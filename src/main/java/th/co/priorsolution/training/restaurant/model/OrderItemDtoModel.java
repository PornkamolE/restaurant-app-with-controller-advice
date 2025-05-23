package th.co.priorsolution.training.restaurant.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import th.co.priorsolution.training.restaurant.entity.FoodCategory;
import th.co.priorsolution.training.restaurant.entity.OrderItemStatus;

@Data
public class OrderItemDtoModel {

    @NotBlank(message = "กรุณาระบุชื่อเมนู")
    private String menuName;

    @NotNull(message = "กรุณาระบุประเภทอาหาร")
    private FoodCategory category;

    @Min(value = 1, message = "ราคาต้องมากกว่า 0")
    private double price;

    @NotNull(message = "กรุณาระบุสถานะของรายการอาหาร")
    private OrderItemStatus status;
}
