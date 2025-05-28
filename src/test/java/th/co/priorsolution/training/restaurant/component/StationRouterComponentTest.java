package th.co.priorsolution.training.restaurant.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import th.co.priorsolution.training.restaurant.entity.FoodCategory;
import th.co.priorsolution.training.restaurant.entity.OrderItemEntity;
import th.co.priorsolution.training.restaurant.service.station.*;

import java.util.List;

import static org.mockito.Mockito.*;

class StationRouterComponentTest {

    private GrillStationService grillStationService;
    private PastaStationService pastaStationService;
    private SaladStationService saladStationService;
    private BeverageStationService beverageStationService;

    private StationRouterComponent stationRouterComponent;

    @BeforeEach
    void setUp() {
        grillStationService = mock(GrillStationService.class);
        pastaStationService = mock(PastaStationService.class);
        saladStationService = mock(SaladStationService.class);
        beverageStationService = mock(BeverageStationService.class);

        stationRouterComponent = new StationRouterComponent(
                grillStationService,
                pastaStationService,
                saladStationService,
                beverageStationService
        );
    }

    @Test
    void testRouteToGrillStation() {
        OrderItemEntity item = new OrderItemEntity();
        item.setCategory(FoodCategory.GRILL);

        stationRouterComponent.routeToStations(List.of(item));

        verify(grillStationService).process(item);
        verifyNoInteractions(pastaStationService, saladStationService, beverageStationService);
    }

    @Test
    void testRouteToPastaStation() {
        OrderItemEntity item = new OrderItemEntity();
        item.setCategory(FoodCategory.PASTA);

        stationRouterComponent.routeToStations(List.of(item));

        verify(pastaStationService).process(item);
        verifyNoInteractions(grillStationService, saladStationService, beverageStationService);
    }

    @Test
    void testRouteToSaladStation() {
        OrderItemEntity item = new OrderItemEntity();
        item.setCategory(FoodCategory.SALAD);

        stationRouterComponent.routeToStations(List.of(item));

        verify(saladStationService).process(item);
        verifyNoInteractions(grillStationService, pastaStationService, beverageStationService);
    }

    @Test
    void testRouteToBeverageStation() {
        OrderItemEntity item = new OrderItemEntity();
        item.setCategory(FoodCategory.BEVERAGE);

        stationRouterComponent.routeToStations(List.of(item));

        verify(beverageStationService).process(item);
        verifyNoInteractions(grillStationService, pastaStationService, saladStationService);
    }

    @Test
    void testMultipleItems_RouteCorrectly() {
        OrderItemEntity grillItem = new OrderItemEntity();
        grillItem.setCategory(FoodCategory.GRILL);

        OrderItemEntity pastaItem = new OrderItemEntity();
        pastaItem.setCategory(FoodCategory.PASTA);

        stationRouterComponent.routeToStations(List.of(grillItem, pastaItem));

        verify(grillStationService).process(grillItem);
        verify(pastaStationService).process(pastaItem);
        verifyNoInteractions(saladStationService, beverageStationService);
    }
}
