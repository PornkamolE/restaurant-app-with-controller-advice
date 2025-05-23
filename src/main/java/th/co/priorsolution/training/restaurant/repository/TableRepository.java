package th.co.priorsolution.training.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import th.co.priorsolution.training.restaurant.entity.TableEntity;
import th.co.priorsolution.training.restaurant.entity.TableStatus;

import java.util.List;

@Repository
public interface TableRepository extends JpaRepository<TableEntity, Integer> {
    List<TableEntity> findByStatus(TableStatus status);
}
