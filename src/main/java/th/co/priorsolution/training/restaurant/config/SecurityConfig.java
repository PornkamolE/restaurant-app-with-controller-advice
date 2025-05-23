package th.co.priorsolution.training.restaurant.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import th.co.priorsolution.training.restaurant.security.JwtAuthenticationEntryPoint;
import th.co.priorsolution.training.restaurant.security.JwtAuthenticationFilter;
import th.co.priorsolution.training.restaurant.service.UserService;


/**
 * Configuration class for Spring Security.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public SecurityConfig(@Lazy UserService userService,
                          JwtAuthenticationFilter jwtAuthenticationFilter,
                          JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.userService = userService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)) // ✅ ปรับตรงนี้
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/order", "/order/**",
                                "/foodOrder.html", "/order-summary.html", "/order-success.html",
                                "/login.html", "/register", "/api/auth/**",
                                "/login-success", "/unauthorized", "/customer/status",
                                "/station/complete","/api/orders"
                        ).permitAll()

                        .requestMatchers("/waitress").hasAuthority("WAITER")
                        .requestMatchers("/manager/dashboard").hasAuthority("MANAGER")

                        .requestMatchers("/station/grill").hasAuthority("CHEF_GRILL")
                        .requestMatchers("/station/pasta").hasAuthority("CHEF_PASTA")
                        .requestMatchers("/station/salad").hasAuthority("CHEF_SALAD")
                        .requestMatchers("/station/beverage").hasAuthority("CHEF_BEVERAGE")


                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login/doLogin")
                        .defaultSuccessUrl("/login-success", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .rememberMe(remember -> remember
                        .key("my-very-secret-remember-me-key") // 🔐 เปลี่ยนตามใจ
                        .tokenValiditySeconds(7 * 24 * 60 * 60) // 7 วัน
                        .userDetailsService(userService)
                );

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}