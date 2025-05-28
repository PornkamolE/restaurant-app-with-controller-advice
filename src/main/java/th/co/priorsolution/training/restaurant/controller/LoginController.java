package th.co.priorsolution.training.restaurant.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import th.co.priorsolution.training.restaurant.security.JwtTokenUtil;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class LoginController {

    private final JwtTokenUtil jwtTokenUtil;

    public LoginController(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/login-success")
    public String loginSuccessRedirect(HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        for (GrantedAuthority authority : auth.getAuthorities()) {
            System.out.println("Role: " + authority.getAuthority());
        }

        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login?error";
        }

        Object principal = auth.getPrincipal();
        if (!(principal instanceof UserDetails)) {
            return "redirect:/login?error";
        }

        UserDetails userDetails = (UserDetails) principal;

        String jwtToken = jwtTokenUtil.generateToken(userDetails);


        // ✅ ใส่ JWT ลงใน Cookie
        ResponseCookie jwtCookie = ResponseCookie.from("jwt", jwtToken)
                .httpOnly(true)
                .secure(false) // ถ้าใช้ HTTPS ให้เปลี่ยนเป็น true
                .path("/")
                .maxAge(7 * 24 * 60 * 60) // 7 วัน
                .sameSite("Strict")
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

        // ✅ redirect ตาม role
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
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
