package th.co.priorsolution.training.restaurant.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import th.co.priorsolution.training.restaurant.entity.Role;
import th.co.priorsolution.training.restaurant.service.UserService;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam Role role,
                               Model model) {
        try {
            userService.registerUser(username, password, role);
            return "redirect:/login"; // ✅ หลังสมัคร → ให้ user ไป login เอง
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register"; // ❌ สมัครไม่สำเร็จ → กลับหน้าเดิม
        }
    }
}
