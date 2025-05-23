package th.co.priorsolution.training.restaurant.model;

import lombok.Data;
import th.co.priorsolution.training.restaurant.entity.OrderItemEntity;
import th.co.priorsolution.training.restaurant.entity.OrderStatus;

import java.util.List;

@Data
public class OrderStatusDtoModel {
    private Long id;
    private int tableNumber;
    private OrderStatus status;
    private List<OrderItemEntity> items;
}
