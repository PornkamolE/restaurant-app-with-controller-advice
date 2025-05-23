package th.co.priorsolution.training.restaurant.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/login-success")
    public String loginSuccessRedirect() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        for (GrantedAuthority authority : auth.getAuthorities()) {
            System.out.println("Role: " + authority.getAuthority()); // ✅ Debug ตรงนี้
        }

        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login?error";
        }

        for (GrantedAuthority authority : auth.getAuthorities()) {
            String role = authority.getAuthority();
            switch (role) {
                case "WAITER":
                    return "redirect:/waitress";
                case "MANAGER":
                    return "redirect:/manager/dashboard";
                case "CHEF_GRILL":
                    return "redirect:/station/grill";
                case "CHEF_PASTA":
                    return "redirect:/station/pasta";
                case "CHEF_SALAD":
                    return "redirect:/station/salad";
                case "CHEF_BEVERAGE":
                    return "redirect:/station/beverage";
            }
        }

        return "redirect:/unauthorized";
    }

    @GetMapping("/unauthorized")
    public String unauthorizedPage() {

        return "unauthorized";
    }


}
