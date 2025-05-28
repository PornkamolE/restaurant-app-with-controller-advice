package th.co.priorsolution.training.restaurant.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import th.co.priorsolution.training.restaurant.component.StationRouterComponent;
import th.co.priorsolution.training.restaurant.entity.*;
import th.co.priorsolution.training.restaurant.exception.ApiOrderNotFoundException;
import th.co.priorsolution.training.restaurant.model.CustomerOrderDtoModel;
import th.co.priorsolution.training.restaurant.model.OrderItemDtoModel;
import th.co.priorsolution.training.restaurant.model.OrderStatusDtoModel;
import th.co.priorsolution.training.restaurant.repository.FoodMenuRepository;
import th.co.priorsolution.training.restaurant.repository.OrderItemRepository;
import th.co.priorsolution.training.restaurant.repository.OrderRepository;
import th.co.priorsolution.training.restaurant.repository.TableRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private OrderItemRepository orderItemRepository;
    @Mock private TableRepository tableRepository;
    @Mock private StationRouterComponent stationRouterComponent;
    @Mock private FoodMenuRepository foodMenuRepository;
    @Mock private TableService tableService;

    @InjectMocks private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetOrderStatus_success() {
        Long orderId = 1L;
        OrderEntity mockOrder = new OrderEntity();
        mockOrder.setId(orderId);
        mockOrder.setTableNumber(5);
        mockOrder.setStatus(OrderStatus.PROCESSING);

        OrderItemEntity item = new OrderItemEntity();
        item.setMenuName("Grilled Chicken");
        item.setStatus(OrderItemStatus.COOKING);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));
        when(orderItemRepository.findByOrderId(orderId)).thenReturn(List.of(item));

        OrderStatusDtoModel result = orderService.getOrderStatus(orderId);

        assertEquals(orderId, result.getId());
        assertEquals(5, result.getTableNumber());
        assertEquals(OrderStatus.PROCESSING, result.getStatus());
        assertEquals(1, result.getItems().size());
        verify(orderRepository).findById(orderId);
        verify(orderItemRepository).findByOrderId(orderId);
    }

    @Test
    void testGetOrderStatus_notFound() {
        Long orderId = 999L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(ApiOrderNotFoundException.class, () -> orderService.getOrderStatus(orderId));
    }

    @Test
    void testCreateOrder_success() {
        // Arrange: สร้าง DTO จำลอง
        CustomerOrderDtoModel dto = new CustomerOrderDtoModel();
        dto.setTableNumber(7);

        OrderItemDtoModel itemDto = new OrderItemDtoModel();
        itemDto.setMenuName("Spaghetti Carbonara");
        itemDto.setCategory(FoodCategory.PASTA);
        dto.setItems(List.of(itemDto));

        // Mock entities
        FoodMenuEntity menu = new FoodMenuEntity();
        menu.setName("Spaghetti Carbonara");
        menu.setPrice(100.0);

        OrderEntity savedOrder = new OrderEntity();
        savedOrder.setId(1L);
        savedOrder.setTableNumber(7);
        savedOrder.setStatus(OrderStatus.NEW);

        // Mock behavior
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(savedOrder);
        when(foodMenuRepository.findByName("Spaghetti Carbonara")).thenReturn(Optional.of(menu));

        // Act
        OrderEntity result = orderService.createOrder(dto);

        // Assert
        assertNotNull(result);
        assertEquals(OrderStatus.PROCESSING, result.getStatus());
        assertEquals(1, result.getItems().size());
        assertEquals("Spaghetti Carbonara", result.getItems().get(0).getMenuName());
        assertEquals(100.0, result.getItems().get(0).getPrice());

        // Verify that dependent services were called
        verify(tableService).markTableOccupied(7);
        verify(orderRepository, times(2)).save(any(OrderEntity.class)); // saved before and after status update
        verify(orderItemRepository).saveAll(anyList());
        verify(stationRouterComponent).routeToStations(anyList());
    }

    @Test
    void testCreateOrder_menuNotFound_shouldThrowException() {
        // Arrange
        CustomerOrderDtoModel dto = new CustomerOrderDtoModel();
        dto.setTableNumber(1);

        OrderItemDtoModel itemDto = new OrderItemDtoModel();
        itemDto.setMenuName("Boat Noodles"); // เมนูนี้ไม่มีใน DB
        itemDto.setCategory(FoodCategory.PASTA);
        dto.setItems(List.of(itemDto));

        when(foodMenuRepository.findByName("Boat Noodles")).thenReturn(Optional.empty());

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(dto);
        });

        assertTrue(exception.getMessage().contains("ไม่พบเมนู"));
    }


}
