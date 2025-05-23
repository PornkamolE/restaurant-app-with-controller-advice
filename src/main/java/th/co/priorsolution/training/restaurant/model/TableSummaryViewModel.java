package th.co.priorsolution.training.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TableSummaryViewModel {
    private Integer tableNumber;
    private List<OrderItemDtoModel> items;
    private double totalPrice;
    private String status;
}
