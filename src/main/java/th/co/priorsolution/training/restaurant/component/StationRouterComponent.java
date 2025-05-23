package th.co.priorsolution.training.restaurant.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import th.co.priorsolution.training.restaurant.entity.FoodCategory;
import th.co.priorsolution.training.restaurant.entity.OrderItemEntity;
import th.co.priorsolution.training.restaurant.service.station.BeverageStationService;
import th.co.priorsolution.training.restaurant.service.station.GrillStationService;
import th.co.priorsolution.training.restaurant.service.station.PastaStationService;
import th.co.priorsolution.training.restaurant.service.station.SaladStationService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StationRouterComponent {

    private final GrillStationService grillStationService;
    private final PastaStationService pastaStationService;
    private final SaladStationService saladStationService;
    private final BeverageStationService beverageStationService;

    public void routeToStations(List<OrderItemEntity> items) {
        for (OrderItemEntity item : items) {
            switch (item.getCategory()) {
                case GRILL -> grillStationService.process(item);
                case PASTA -> pastaStationService.process(item);
                case SALAD -> saladStationService.process(item);
                case BEVERAGE -> beverageStationService.process(item);
            }
        }
    }
}

