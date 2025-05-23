package th.co.priorsolution.training.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import th.co.priorsolution.training.restaurant.entity.FoodCategory;
import th.co.priorsolution.training.restaurant.entity.OrderItemEntity;
import th.co.priorsolution.training.restaurant.entity.OrderItemStatus;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
    List<OrderItemEntity> findByOrderId(Long orderId); // ใช้ดึงรายการอาหารตาม// Order
    List<OrderItemEntity> findByCategoryAndStatus(FoodCategory category, OrderItemStatus status);
    List<OrderItemEntity> findByStatus(OrderItemStatus status);

}
