package th.co.priorsolution.training.restaurant.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "order_items")
@Data
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private OrderEntity order;

    private String menuName;

    @Enumerated(EnumType.STRING)
    private FoodCategory category;

    private double price;

    @Enumerated(EnumType.STRING)
    private OrderItemStatus status = OrderItemStatus.NEW;
}