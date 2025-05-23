package th.co.priorsolution.training.restaurant.model;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CustomerOrderDtoModel {

    @NotNull(message = "กรุณาระบุหมายเลขโต๊ะ")
    @Min(value = 1, message = "หมายเลขโต๊ะต้องมากกว่า 0")
    private Integer tableNumber;

    @NotNull(message = "ต้องมีรายการอาหาร")
    @Valid
    private List<OrderItemDtoModel> items;

}
