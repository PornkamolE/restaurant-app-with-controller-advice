package th.co.priorsolution.training.restaurant.exception;

public class FoodMenuNotFoundException extends RuntimeException {

    public FoodMenuNotFoundException(String message) {
      super(message);
    }
}
