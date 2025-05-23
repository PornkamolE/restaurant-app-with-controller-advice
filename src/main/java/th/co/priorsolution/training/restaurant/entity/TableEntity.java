package th.co.priorsolution.training.restaurant.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tables")
@Data
public class TableEntity {
    @Id
    @Column(name = "table_number")
    private int tableNumber;

    @Enumerated(EnumType.STRING)
    private TableStatus status;
}
