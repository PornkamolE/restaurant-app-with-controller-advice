package th.co.priorsolution.training.restaurant.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import th.co.priorsolution.training.restaurant.entity.TableEntity;
import th.co.priorsolution.training.restaurant.entity.TableStatus;
import th.co.priorsolution.training.restaurant.repository.TableRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TableServiceTest {

    @Mock
    private TableRepository tableRepository;

    @InjectMocks
    private TableService tableService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void markTableOccupied_shouldSetStatusToOccupied() {
        TableEntity table = new TableEntity();
        table.setTableNumber(1);

        when(tableRepository.findById(1)).thenReturn(Optional.of(table));

        tableService.markTableOccupied(1);

        assertEquals(TableStatus.OCCUPIED, table.getStatus());
        verify(tableRepository).save(table);
    }

    @Test
    void markTableCompleted_shouldSetStatusToCompleted() {
        TableEntity table = new TableEntity();
        table.setTableNumber(2);

        when(tableRepository.findById(2)).thenReturn(Optional.of(table));

        tableService.markTableCompleted(2);

        assertEquals(TableStatus.COMPLETED, table.getStatus());
        verify(tableRepository).save(table);
    }

    @Test
    void markTableAvailable_shouldSetStatusToAvailable() {
        TableEntity table = new TableEntity();
        table.setTableNumber(3);

        when(tableRepository.findById(3)).thenReturn(Optional.of(table));

        tableService.markTableAvailable(3);

        assertEquals(TableStatus.AVAILABLE, table.getStatus());
        verify(tableRepository).save(table);
    }

    @Test
    void getTable_shouldThrowException_whenTableNotFound() {
        when(tableRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            tableService.getTable(99);
        });

        assertEquals("Table not found: 99", exception.getMessage());
    }
}
