package th.co.priorsolution.training.restaurant.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import th.co.priorsolution.training.restaurant.entity.OrderEntity;
import th.co.priorsolution.training.restaurant.entity.OrderItemEntity;
import th.co.priorsolution.training.restaurant.entity.OrderItemStatus;
import th.co.priorsolution.training.restaurant.entity.OrderStatus;
import th.co.priorsolution.training.restaurant.repository.OrderItemRepository;
import th.co.priorsolution.training.restaurant.repository.OrderRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StationServiceTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private StationService stationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void markItemAsReady_shouldUpdateItemOnly_whenNotAllItemsAreReady() {
        // Arrange
        OrderEntity order = new OrderEntity();
        order.setStatus(OrderStatus.PROCESSING);

        OrderItemEntity readyItem = new OrderItemEntity();
        readyItem.setId(1L);
        readyItem.setStatus(OrderItemStatus.COOKING);
        readyItem.setOrder(order);

        OrderItemEntity notReadyItem = new OrderItemEntity();
        notReadyItem.setStatus(OrderItemStatus.COOKING);
        notReadyItem.setOrder(order);

        order.setItems(List.of(readyItem, notReadyItem));

        when(orderItemRepository.findById(1L)).thenReturn(Optional.of(readyItem));

        // Act
        stationService.markItemAsReady(1L);

        // Assert
        assertEquals(OrderItemStatus.READY, readyItem.getStatus());
        assertEquals(OrderStatus.PROCESSING, order.getStatus()); // ยังไม่เปลี่ยนเป็น DONE

        verify(orderItemRepository).save(readyItem);
        verify(orderRepository, never()).save(order);
    }

    @Test
    void markItemAsReady_shouldUpdateOrderStatus_whenAllItemsAreReady() {
        // Arrange
        OrderEntity order = new OrderEntity();
        order.setStatus(OrderStatus.PROCESSING);

        OrderItemEntity readyItem1 = new OrderItemEntity();
        readyItem1.setStatus(OrderItemStatus.READY);
        readyItem1.setOrder(order);

        OrderItemEntity readyItem2 = new OrderItemEntity();
        readyItem2.setId(2L);
        readyItem2.setStatus(OrderItemStatus.COOKING);
        readyItem2.setOrder(order);

        order.setItems(List.of(readyItem1, readyItem2));

        when(orderItemRepository.findById(2L)).thenReturn(Optional.of(readyItem2));

        // Act
        stationService.markItemAsReady(2L);

        // Assert
        assertEquals(OrderItemStatus.READY, readyItem2.getStatus());
        assertEquals(OrderStatus.DONE, order.getStatus());

        verify(orderItemRepository).save(readyItem2);
        verify(orderRepository).save(order); // ✅ ต้อง save เพราะ order เสร็จแล้ว
    }
}
