package th.co.priorsolution.training.restaurant.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import th.co.priorsolution.training.restaurant.entity.OrderEntity;
import th.co.priorsolution.training.restaurant.exception.OrderViewNotFoundException;
import th.co.priorsolution.training.restaurant.model.OrderItemDtoModel;
import th.co.priorsolution.training.restaurant.repository.OrderRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final OrderRepository orderRepository;

    @GetMapping("/status")
    public String viewOrderStatus(@RequestParam(value = "orderId", required = false) Long orderId,
                                  @RequestParam(value = "tableNumber", required = false) Integer tableNumber,
                                  Model model) {

        if (orderId == null) {
            throw new OrderViewNotFoundException("กรุณาระบุหมายเลขออเดอร์");
        }

        // 1. ดึงออเดอร์จาก DB
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderViewNotFoundException("ไม่พบออเดอร์หมายเลข " + orderId));

        Integer actualTableNumber = order.getTableNumber();

        // 2. ถ้ามีการกรอกเลขโต๊ะ → ตรวจสอบความถูกต้อง
        if (tableNumber != null && !Objects.equals(actualTableNumber, tableNumber)) {
            throw new OrderViewNotFoundException("ออเดอร์หมายเลข " + orderId + " ไม่ใช่ของโต๊ะที่คุณระบุ (" + tableNumber + ")");
        }

        // 3. แปลงรายการอาหารเป็น DTO
        List<OrderItemDtoModel> itemDtos = order.getItems().stream().map(item -> {
            OrderItemDtoModel dto = new OrderItemDtoModel();
            dto.setMenuName(item.getMenuName());
            dto.setCategory(item.getCategory());
            dto.setStatus(item.getStatus());
            return dto;
        }).toList();

        // 4. ใส่ข้อมูลลงใน model เพื่อแสดงใน Thymeleaf
        model.addAttribute("orderItems", itemDtos);
        model.addAttribute("orderId", order.getId());
        model.addAttribute("tableNumber", actualTableNumber);

        return "customer-status";
    }


}
