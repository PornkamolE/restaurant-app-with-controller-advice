package th.co.priorsolution.training.restaurant.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import th.co.priorsolution.training.restaurant.entity.OrderEntity;
import th.co.priorsolution.training.restaurant.model.OrderItemDtoModel;
import th.co.priorsolution.training.restaurant.model.TableSummaryViewModel;
import th.co.priorsolution.training.restaurant.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final OrderRepository orderRepository;

    @GetMapping("/dashboard")
    public String viewDashboard(Model model) {
        List<OrderEntity> allOrders = orderRepository.findAll();

        List<TableSummaryViewModel> tableSummaries = new ArrayList<>();
        double grandTotal = 0;

        for (OrderEntity order : allOrders) {
            List<OrderItemDtoModel> items = order.getItems().stream().map(item -> {
                OrderItemDtoModel dto = new OrderItemDtoModel();
                dto.setMenuName(item.getMenuName());
                dto.setCategory(item.getCategory());
                dto.setPrice(item.getPrice());
                return dto;
            }).toList();

            double totalPrice = items.stream().mapToDouble(OrderItemDtoModel::getPrice).sum();
            grandTotal += totalPrice;

            boolean allServed = order.getItems().stream()
                    .allMatch(item -> item.getStatus().name().equals("SERVED"));

            String status = allServed ? "DONE" : order.getStatus().name();

            tableSummaries.add(new TableSummaryViewModel(order.getTableNumber(), items, totalPrice, status));
        }

        model.addAttribute("tableSummaries", tableSummaries);
        model.addAttribute("grandTotal", grandTotal);
        return "manager-dashboard";
    }
}
