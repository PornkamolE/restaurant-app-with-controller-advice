package th.co.priorsolution.training.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import th.co.priorsolution.training.restaurant.entity.*;
import th.co.priorsolution.training.restaurant.model.CustomerOrderDtoModel;
import th.co.priorsolution.training.restaurant.model.OrderItemDtoModel;
import th.co.priorsolution.training.restaurant.repository.FoodMenuRepository;
import th.co.priorsolution.training.restaurant.repository.TableRepository;
import th.co.priorsolution.training.restaurant.service.OrderService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class AppController {

    @Autowired
    private FoodMenuRepository foodMenuRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private TableRepository tableRepository;

    @GetMapping("/order")
    public String orderForm(Model model) {
        // สร้าง order model เปล่าไว้สำหรับ binding form
        CustomerOrderDtoModel order = new CustomerOrderDtoModel();
        order.setItems(List.of(new OrderItemDtoModel()));

        // ดึงเมนูทั้งหมด
        List<FoodMenuEntity> foodMenuList = foodMenuRepository.findAll();

        // ดึงเฉพาะโต๊ะที่ยังว่าง
        List<TableEntity> availableTables = tableRepository.findByStatus(TableStatus.AVAILABLE);

        Set<FoodCategory> menuCategories = foodMenuList.stream()
                .map(FoodMenuEntity::getCategory)
                .collect(Collectors.toSet());

        // ส่งข้อมูลไปยัง view
        model.addAttribute("order", order);
        model.addAttribute("foodMenuList", foodMenuList);
        model.addAttribute("availableTables", availableTables);
        model.addAttribute("menuCategories", menuCategories);
        return "foodOrder"; // foodOrder.html
    }


    @PostMapping("/order/submit")
    public String submitOrder(@ModelAttribute CustomerOrderDtoModel order, Model model) {

        // เติมราคาจาก foodMenuRepository
        for (OrderItemDtoModel item : order.getItems()) {
            FoodMenuEntity menu = foodMenuRepository.findByName(item.getMenuName())
                    .orElseThrow(() -> new IllegalArgumentException("ไม่พบเมนู: " + item.getMenuName()));
            item.setPrice(menu.getPrice());
        }

        // คำนวณรวม
        double totalPrice = order.getItems().stream()
                .mapToDouble(OrderItemDtoModel::getPrice)
                .sum();

        // ✅ ยังไม่บันทึกลง DB — แค่ส่งข้อมูลไปแสดงใน order-summary
        model.addAttribute("orderSummary", order);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("tableNumber", order.getTableNumber());

        return "order-summary";
    }


    @PostMapping("/order/confirm")
    public String confirmOrder(@ModelAttribute CustomerOrderDtoModel order, Model model) {
        OrderEntity savedOrder = orderService.createOrder(order);

        model.addAttribute("orderId", savedOrder.getId());
        model.addAttribute("tableNumber", savedOrder.getTableNumber());
        model.addAttribute("successMessage", "ออเดอร์ถูกบันทึกเรียบร้อยแล้ว!");
        return "order-success";
    }

}




