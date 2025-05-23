package th.co.priorsolution.training.restaurant.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import th.co.priorsolution.training.restaurant.entity.TableEntity;
import th.co.priorsolution.training.restaurant.entity.TableStatus;
import th.co.priorsolution.training.restaurant.repository.TableRepository;

@Service
@RequiredArgsConstructor
public class TableService {

    private final TableRepository tableRepository;

    public TableEntity getTable(int tableNumber) {
        return tableRepository.findById(tableNumber)
                .orElseThrow(() -> new RuntimeException("Table not found: " + tableNumber));
    }

    public void markTableCompleted(int tableNumber) {
        TableEntity table = getTable(tableNumber);
        table.setStatus(TableStatus.COMPLETED);
        tableRepository.save(table);
    }

    public void markTableOccupied(int tableNumber) {
        TableEntity table = getTable(tableNumber);
        table.setStatus(TableStatus.OCCUPIED);
        tableRepository.save(table);
    }

    public void markTableAvailable(int tableNumber) {
        TableEntity table = getTable(tableNumber);
        table.setStatus(TableStatus.AVAILABLE);
        tableRepository.save(table);
    }
}
