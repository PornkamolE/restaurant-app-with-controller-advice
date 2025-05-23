package th.co.priorsolution.training.restaurant.service;

import org.springframework.stereotype.Service;
import th.co.priorsolution.training.restaurant.entity.OrderEntity;

@Service
public class ManagerService {
    public void notify(OrderEntity orderEntity){
        System.out.println("Manager notified with order summary for table "+ orderEntity.getTableNumber());
    }
}
