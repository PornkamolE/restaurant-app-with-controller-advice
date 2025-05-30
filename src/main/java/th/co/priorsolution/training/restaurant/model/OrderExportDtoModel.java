package th.co.priorsolution.training.restaurant.model;

import lombok.Data;

@Data
public class OrderExportDtoModel {
    private Long orderId;
    private int tableNumber;
    private String createdAt;
    private String status;
    private String menuName;
    private double price;
}