package th.co.priorsolution.training.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import th.co.priorsolution.training.restaurant.entity.FoodMenuEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodMenuRepository extends JpaRepository<FoodMenuEntity, Long> {
    List<FoodMenuEntity> findAll();
    Optional<FoodMenuEntity> findByName(String name);

}
