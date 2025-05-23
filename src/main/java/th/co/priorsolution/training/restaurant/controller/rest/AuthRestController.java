package th.co.priorsolution.training.restaurant.controller.rest;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import th.co.priorsolution.training.restaurant.entity.Role;
import th.co.priorsolution.training.restaurant.entity.UserEntity;
import th.co.priorsolution.training.restaurant.model.AuthRequest;
import th.co.priorsolution.training.restaurant.model.AuthResponse;
import th.co.priorsolution.training.restaurant.security.JwtTokenUtil;
import th.co.priorsolution.training.restaurant.service.UserService;

/**
 * Controller for authentication endpoints.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AuthRestController(AuthenticationManager authenticationManager,
                          UserService userService,
                          JwtTokenUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /**
     * Login endpoint.
     *
     * @param authRequest The authentication request
     * @return The authentication response with JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        long startTime = System.currentTimeMillis();

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtTokenUtil.generateToken(userDetails);

            long processingTime = System.currentTimeMillis() - startTime;

            return ResponseEntity.ok(new AuthResponse(token, userDetails.getUsername(), processingTime));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    /**
     * Registration endpoint.
     *
     * @param authRequest The registration request
     * @return The authentication response with JWT token
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthRequest authRequest) {
        long startTime = System.currentTimeMillis();

        UserEntity user = userService.registerUser(
                authRequest.getUsername(),
                authRequest.getPassword(),
                Role.valueOf(authRequest.getRole()));

        UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);

        long processingTime = System.currentTimeMillis() - startTime;

        return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), processingTime));
    }
}