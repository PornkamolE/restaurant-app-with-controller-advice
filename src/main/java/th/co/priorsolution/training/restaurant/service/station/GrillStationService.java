package th.co.priorsolution.training.restaurant.service.station;

import org.springframework.stereotype.Service;
import th.co.priorsolution.training.restaurant.entity.OrderItemEntity;
import th.co.priorsolution.training.restaurant.entity.OrderItemStatus;

import java.sql.SQLOutput;

@Service
public class GrillStationService {

    public void process(OrderItemEntity orderItemEntity){
        orderItemEntity.setStatus(OrderItemStatus.COOKING);

        System.out.println("Grilling: "+orderItemEntity.getMenuName());
        //orderItemEntity.setStatus(OrderItemStatus.READY);
    }
}
