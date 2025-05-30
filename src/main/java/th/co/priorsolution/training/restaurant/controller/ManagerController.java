package th.co.priorsolution.training.restaurant.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import th.co.priorsolution.training.restaurant.entity.OrderEntity;
import th.co.priorsolution.training.restaurant.entity.OrderItemEntity;
import th.co.priorsolution.training.restaurant.model.OrderItemDtoModel;
import th.co.priorsolution.training.restaurant.model.TableSummaryViewModel;
import th.co.priorsolution.training.restaurant.repository.OrderRepository;
import th.co.priorsolution.training.restaurant.service.ManagerService;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;
    private final OrderRepository orderRepository;

    @GetMapping("/dashboard")
    public String viewDashboard(
            @RequestParam(value = "viewType", defaultValue = "ALL") String viewType,
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "month", required = false) Integer month,
            Model model) {

        List<OrderEntity> allOrders = orderRepository.findAll();
        List<OrderEntity> filteredOrders = new ArrayList<>();

        if ("DAILY".equals(viewType) && date != null) {
            filteredOrders = allOrders.stream()
                    .filter(o -> o.getCreatedAt().toLocalDate().isEqual(date))
                    .toList();
        } else if ("MONTHLY".equals(viewType) && year != null && month != null) {
            filteredOrders = allOrders.stream()
                    .filter(o -> o.getCreatedAt().getYear() == year &&
                            o.getCreatedAt().getMonthValue() == month)
                    .toList();
        } else {
            filteredOrders = allOrders;
        }

        List<TableSummaryViewModel> tableSummaries = new ArrayList<>();
        double grandTotal = 0;

        for (OrderEntity order : filteredOrders) {
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

        model.addAttribute("viewType", viewType);
        model.addAttribute("date", date);
        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("tableSummaries", tableSummaries);
        model.addAttribute("grandTotal", grandTotal);
        return "manager-dashboard";
    }


    @GetMapping("/export/csv")
    public ResponseEntity<Resource> exportCSV() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        managerService.exportOrdersToCSV(out);
        ByteArrayResource resource = new ByteArrayResource(out.toByteArray());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=orders.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
    }

    @GetMapping("/export/excel")
    public ResponseEntity<Resource> exportExcel() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        managerService.exportOrdersToExcel(out);
        ByteArrayResource resource = new ByteArrayResource(out.toByteArray());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=orders.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(resource);
    }

    @GetMapping("/export/pdf")
    public ResponseEntity<Resource> exportPdf() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        managerService.exportOrdersToJasper("/orders.jrxml", out);
        ByteArrayResource resource = new ByteArrayResource(out.toByteArray());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=orders.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @GetMapping("/revenue/daily")
    public String dailyRevenue(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                               Model model) {
        double total = managerService.calculateTotalRevenueForDate(date);
        model.addAttribute("date", date);
        model.addAttribute("revenue", total);
        return "revenue-daily";
    }

    @GetMapping("/revenue/monthly")
    public String monthlyRevenue(@RequestParam int year, @RequestParam int month, Model model) {
        double total = managerService.calculateTotalRevenueForMonth(year, month);
        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("revenue", total);
        return "revenue-monthly";
    }
}
