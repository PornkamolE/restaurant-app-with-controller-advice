package th.co.priorsolution.training.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import th.co.priorsolution.training.restaurant.entity.FoodCategory;
import th.co.priorsolution.training.restaurant.entity.OrderItemEntity;
import th.co.priorsolution.training.restaurant.entity.OrderItemStatus;
import th.co.priorsolution.training.restaurant.repository.OrderItemRepository;
import th.co.priorsolution.training.restaurant.service.StationService;

import java.util.List;

@Controller
@RequestMapping("/station")
public class StationController {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private StationService stationService;

    @GetMapping("/{type}")
    public String stationPage(@org.springframework.web.bind.annotation.PathVariable String type, Model model) {
        FoodCategory category = getCategoryFromType(type); // แปลง String เป็น enum
        List<OrderItemEntity> items = orderItemRepository.findByCategoryAndStatus(category, OrderItemStatus.COOKING);

        model.addAttribute("items", items);
        return "station-" + type; // เช่น station-grill.html, station-pasta.html
    }

    // กดปุ่มทำอาหารเสร็จ → redirect กลับสถานีเดิม
    @PostMapping("/complete")
    public String completeItem(@RequestParam Long orderItemId, @RequestParam String stationType) {
        stationService.markItemAsReady(orderItemId);
        return "redirect:/station/" + stationType;
    }

    // Utility method แปลง string เป็น enum (ปลอดภัย)
    private FoodCategory getCategoryFromType(String type) {
        return switch (type.toLowerCase()) {
            case "grill" -> FoodCategory.GRILL;
            case "pasta" -> FoodCategory.PASTA;
            case "salad" -> FoodCategory.SALAD;
            case "beverage" -> FoodCategory.BEVERAGE;
            default -> throw new IllegalArgumentException("Invalid station type: " + type);
        };
    }
}

