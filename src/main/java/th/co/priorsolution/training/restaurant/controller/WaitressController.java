package th.co.priorsolution.training.restaurant.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import th.co.priorsolution.training.restaurant.entity.OrderItemEntity;
import th.co.priorsolution.training.restaurant.entity.OrderItemStatus;
import th.co.priorsolution.training.restaurant.entity.TableEntity;
import th.co.priorsolution.training.restaurant.entity.TableStatus;
import th.co.priorsolution.training.restaurant.repository.OrderItemRepository;
import th.co.priorsolution.training.restaurant.repository.TableRepository;
import th.co.priorsolution.training.restaurant.service.WaitressService;

import java.util.List;

@Controller
@RequestMapping("/waitress")
public class WaitressController {
    private final OrderItemRepository orderItemRepository;
    private final TableRepository tableRepository;
    private final WaitressService waitressService;

    public WaitressController(OrderItemRepository itemRepo, TableRepository tableRepo, WaitressService service) {
        this.orderItemRepository = itemRepo;
        this.tableRepository = tableRepo;
        this.waitressService = service;
    }

    @GetMapping
    public String viewReadyItems(Model model) {
        List<OrderItemEntity> readyItems = orderItemRepository.findByStatus(OrderItemStatus.READY);
        List<TableEntity> completedTables = tableRepository.findByStatus(TableStatus.COMPLETED);

        model.addAttribute("readyItems", readyItems);
        model.addAttribute("completedTables", completedTables);
        return "waitress-view";
    }

    @PostMapping("/serve")
    public String serveItem(@RequestParam Long orderItemId) {
        waitressService.serve(orderItemId);
        return "redirect:/waitress";
    }

    @PostMapping("/clean")
    public String cleanTable(@RequestParam Integer tableNumber) {
        waitressService.cleanTable(tableNumber);
        return "redirect:/waitress";
    }
}
