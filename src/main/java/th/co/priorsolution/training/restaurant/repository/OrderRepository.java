package th.co.priorsolution.training.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import th.co.priorsolution.training.restaurant.entity.OrderEntity;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findTopByTableNumberOrderByCreatedAtDesc(Integer tableNumber);

}
