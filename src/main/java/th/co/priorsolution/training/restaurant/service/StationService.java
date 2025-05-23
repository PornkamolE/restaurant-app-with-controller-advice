package th.co.priorsolution.training.restaurant.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.priorsolution.training.restaurant.entity.OrderEntity;
import th.co.priorsolution.training.restaurant.entity.OrderItemEntity;
import th.co.priorsolution.training.restaurant.entity.OrderItemStatus;
import th.co.priorsolution.training.restaurant.entity.OrderStatus;
import th.co.priorsolution.training.restaurant.repository.OrderItemRepository;
import th.co.priorsolution.training.restaurant.repository.OrderRepository;

@Service
public class StationService {
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public void markItemAsReady(Long itemId) {
        OrderItemEntity item = orderItemRepository.findById(itemId).orElseThrow();
        item.setStatus(OrderItemStatus.READY);
        orderItemRepository.save(item);

        // ตรวจสอบว่าออเดอร์นี้เมนูทุกชิ้น READY แล้วหรือยัง
        OrderEntity order = item.getOrder();
        boolean allReady = order.getItems().stream()
                .allMatch(i -> i.getStatus() == OrderItemStatus.READY);

        if (allReady) {
            order.setStatus(OrderStatus.DONE);
            orderRepository.save(order);
        }
    }
}
