package th.co.priorsolution.training.restaurant.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import th.co.priorsolution.training.restaurant.model.ResponseModel;
import th.co.priorsolution.training.restaurant.repository.OrderRepository;
import th.co.priorsolution.training.restaurant.service.WaitressService;

@RestController
@RequestMapping("/api/waitress")
@RequiredArgsConstructor
public class WaitressRestController {

    private final WaitressService waitressService;
    private final OrderRepository orderRepository;

    @PostMapping("/serve-item/{orderItemId}")
    public ResponseEntity<ResponseModel<String>> serveItem(@PathVariable Long orderItemId) {
        ResponseModel<String> result = new ResponseModel<>();

        try {
            waitressService.serve(orderItemId);

            result.setStatus(200);
            result.setDescription("Served successfully");
            result.setData("OrderItem ID " + orderItemId + " marked as SERVED");

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription("Error serving item: " + e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
    }

    @PostMapping("/clean-table/{tableNumber}")
    public ResponseEntity<ResponseModel<String>> cleanTable(@PathVariable Integer tableNumber) {
        ResponseModel<String> result = new ResponseModel<>();

        try {
            waitressService.cleanTable(tableNumber);

            result.setStatus(200);
            result.setDescription("Table cleaned successfully");
            result.setData("Table " + tableNumber + " is now AVAILABLE");

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription("Error cleaning table: " + e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
    }
}
