package th.co.priorsolution.training.restaurant.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import th.co.priorsolution.training.restaurant.exception.OrderNotFoundException;
import th.co.priorsolution.training.restaurant.exception.OrderViewNotFoundException;


@ControllerAdvice(basePackages = "th.co.priorsolution.training.restaurant.controller")
public class ExceptionHandlerController {

    @ExceptionHandler(OrderNotFoundException.class)
    public String handleOrderNotFound(OrderNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error-page";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error-page";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneric(Exception ex, Model model) {
        model.addAttribute("errorMessage", "เกิดข้อผิดพลาด: " + ex.getMessage());
        return "error-page";
    }

    @ExceptionHandler(OrderViewNotFoundException.class)
    public String handleOrderViewError(OrderViewNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "customer-status";
    }
}
