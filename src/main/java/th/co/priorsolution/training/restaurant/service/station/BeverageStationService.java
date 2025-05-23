package th.co.priorsolution.training.restaurant.service.station;

import org.springframework.stereotype.Service;
import th.co.priorsolution.training.restaurant.entity.OrderItemEntity;
import th.co.priorsolution.training.restaurant.entity.OrderItemStatus;

@Service
public class BeverageStationService {

    public void process(OrderItemEntity orderItemEntity){
        orderItemEntity.setStatus(OrderItemStatus.COOKING);

        System.out.println("Mixing Beverage: "+orderItemEntity.getMenuName());
        //orderItemEntity.setStatus(OrderItemStatus.READY);
    }
}
