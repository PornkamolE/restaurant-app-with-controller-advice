package th.co.priorsolution.training.restaurant.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import th.co.priorsolution.training.restaurant.entity.*;
import th.co.priorsolution.training.restaurant.repository.OrderItemRepository;
import th.co.priorsolution.training.restaurant.repository.TableRepository;

@Service
@Slf4j
public class WaitressService {
    private final OrderItemRepository orderItemRepository;
    private final TableRepository tableRepository;
    private final TableService tableService;

    public WaitressService(OrderItemRepository orderItemRepo, TableRepository tableRepo, TableService tableService) {
        this.orderItemRepository = orderItemRepo;
        this.tableRepository = tableRepo;
        this.tableService = tableService;
    }

    @Transactional
    public void serve(Long orderItemId) {
        OrderItemEntity item = orderItemRepository.findById(orderItemId).orElseThrow();
        item.setStatus(OrderItemStatus.SERVED);
        orderItemRepository.save(item);

        OrderEntity order = item.getOrder();

        boolean allServed = order.getItems().stream()
                .allMatch(i -> i.getStatus() == OrderItemStatus.SERVED);

        if (allServed) {
            tableService.markTableCompleted(order.getTableNumber());
        }
    }

    @Transactional
    public void cleanTable(Integer tableNumber) {
        tableService.markTableAvailable(tableNumber);
    }


}
