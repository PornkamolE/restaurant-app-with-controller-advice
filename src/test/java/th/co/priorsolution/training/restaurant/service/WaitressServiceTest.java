package th.co.priorsolution.training.restaurant.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import th.co.priorsolution.training.restaurant.entity.*;
import th.co.priorsolution.training.restaurant.repository.OrderItemRepository;
import th.co.priorsolution.training.restaurant.repository.TableRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WaitressServiceTest {

    @Mock private OrderItemRepository orderItemRepository;
    @Mock private TableRepository tableRepository;
    @Mock private TableService tableService;

    @InjectMocks private WaitressService waitressService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void serve_shouldUpdateItemStatusAndNotCallTableService_whenNotAllItemsServed() {
        // Arrange
        OrderEntity order = new OrderEntity();
        order.setTableNumber(3);

        OrderItemEntity item1 = new OrderItemEntity();
        item1.setId(1L);
        item1.setStatus(OrderItemStatus.READY);
        item1.setOrder(order);

        OrderItemEntity item2 = new OrderItemEntity();
        item2.setStatus(OrderItemStatus.READY);
        item2.setOrder(order);

        order.setItems(List.of(item1, item2));

        when(orderItemRepository.findById(1L)).thenReturn(Optional.of(item1));

        // Act
        waitressService.serve(1L);

        // Assert
        assertEquals(OrderItemStatus.SERVED, item1.getStatus());
        verify(orderItemRepository).save(item1);
        verify(tableService, never()).markTableCompleted(anyInt());
    }

    @Test
    void serve_shouldCallMarkTableCompleted_whenAllItemsServed() {
        // Arrange
        OrderEntity order = new OrderEntity();
        order.setTableNumber(5);

        OrderItemEntity item1 = new OrderItemEntity();
        item1.setId(1L);
        item1.setStatus(OrderItemStatus.READY);
        item1.setOrder(order);

        OrderItemEntity item2 = new OrderItemEntity();
        item2.setStatus(OrderItemStatus.SERVED); // already served
        item2.setOrder(order);

        order.setItems(List.of(item1, item2));

        when(orderItemRepository.findById(1L)).thenReturn(Optional.of(item1));

        // Act
        waitressService.serve(1L);

        // Assert
        assertEquals(OrderItemStatus.SERVED, item1.getStatus());
        verify(orderItemRepository).save(item1);
        verify(tableService).markTableCompleted(5); // ✅ เรียกเมื่อเสิร์ฟครบ
    }

    @Test
    void serve_shouldThrowException_whenOrderItemNotFound() {
        // Arrange
        when(orderItemRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> waitressService.serve(999L));
    }

    @Test
    void cleanTable_shouldCallMarkTableAvailable() {
        // Act
        waitressService.cleanTable(7);

        // Assert
        verify(tableService).markTableAvailable(7);
    }
}
