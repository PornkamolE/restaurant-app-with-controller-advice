package th.co.priorsolution.training.restaurant.restcontroller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import th.co.priorsolution.training.restaurant.entity.FoodMenuEntity;
import th.co.priorsolution.training.restaurant.model.ResponseModel;
import th.co.priorsolution.training.restaurant.service.FoodMenuService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FoodMenuRestController {

    private final FoodMenuService foodMenuService;

    public FoodMenuRestController(FoodMenuService foodMenuService) {

        this.foodMenuService = foodMenuService;
    }

    @GetMapping("/menu")
    public ResponseModel<List<FoodMenuEntity>> getMenu(){

        return this.foodMenuService.getAllMenu();
    }
}
