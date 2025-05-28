package th.co.priorsolution.training.restaurant.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import th.co.priorsolution.training.restaurant.entity.FoodMenuEntity;
import th.co.priorsolution.training.restaurant.exception.FoodMenuNotFoundException;
import th.co.priorsolution.training.restaurant.model.ResponseModel;
import th.co.priorsolution.training.restaurant.repository.FoodMenuRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FoodMenuServiceTest {

    @Mock
    private FoodMenuRepository foodMenuRepository;

    @InjectMocks
    private FoodMenuService foodMenuService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllMenu_shouldReturnMenuList_whenMenuExists() {
        // Arrange
        FoodMenuEntity menu = new FoodMenuEntity();
        menu.setName("Grilled Chicken");
        when(foodMenuRepository.findAll()).thenReturn(List.of(menu));

        // Act
        ResponseModel<List<FoodMenuEntity>> result = foodMenuService.getAllMenu();

        // Assert
        assertEquals(200, result.getStatus());
        assertEquals("ok", result.getDescription());
        assertEquals(1, result.getData().size());
        assertEquals("Grilled Chicken", result.getData().get(0).getName());
        verify(foodMenuRepository).findAll();
    }

    @Test
    void getAllMenu_shouldThrowException_whenMenuIsEmpty() {
        // Arrange
        when(foodMenuRepository.findAll()).thenReturn(List.of());

        // Act & Assert
        assertThrows(FoodMenuNotFoundException.class, () -> {
            foodMenuService.getAllMenu();
        });
        verify(foodMenuRepository).findAll();
    }
}
