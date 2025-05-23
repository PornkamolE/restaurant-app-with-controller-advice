package th.co.priorsolution.training.restaurant.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "food_menu")
@Entity
public class FoodMenuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private FoodCategory category;

    private Double price;

    @Column(name = "image_url")
    private String imageUrl;

}
