package th.co.priorsolution.training.restaurant.exception;

public class ApiOrderNotFoundException extends RuntimeException {

  public ApiOrderNotFoundException(Long orderId) {
    super("ไม่พบออเดอร์หมายเลข: " + orderId);
  }
}
