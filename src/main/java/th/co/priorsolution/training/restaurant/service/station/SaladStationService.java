package th.co.priorsolution.training.restaurant.service.station;

import org.springframework.stereotype.Service;
import th.co.priorsolution.training.restaurant.entity.OrderItemEntity;
import th.co.priorsolution.training.restaurant.entity.OrderItemStatus;

@Service
public class SaladStationService {
    public void process(OrderItemEntity orderItemEntity){
        orderItemEntity.setStatus(OrderItemStatus.COOKING);

        System.out.println("Preparing Salad: "+orderItemEntity.getMenuName());
        //orderItemEntity.setStatus(OrderItemStatus.READY);
    }
}
