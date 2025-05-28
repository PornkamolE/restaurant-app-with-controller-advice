package th.co.priorsolution.training.restaurant.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import th.co.priorsolution.training.restaurant.entity.Role;
import th.co.priorsolution.training.restaurant.entity.UserEntity;
import th.co.priorsolution.training.restaurant.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //Test: โหลด User สำเร็จ
    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenUserExists() {
        UserEntity user = new UserEntity();
        user.setUsername("admin");
        user.setPassword("hashedpassword");
        user.setRole(Role.ADMIN);
        user.setEnabled(true);

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));

        UserDetails result = userService.loadUserByUsername("admin");

        assertEquals("admin", result.getUsername());
        assertEquals("hashedpassword", result.getPassword());
        assertTrue(result.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ADMIN")));
    }

    //Test: ไม่เจอ user
    @Test
    void loadUserByUsername_shouldThrowException_whenUserNotFound() {
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("ghost");
        });
    }

    //Test: ลงทะเบียนสำเร็จ
    @Test
    void registerUser_shouldCreateNewUser_whenUsernameAvailable() {
        String rawPassword = "1234";
        String encodedPassword = "encoded-1234";

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        UserEntity savedUser = new UserEntity();
        savedUser.setUsername("newuser");
        savedUser.setPassword(encodedPassword);
        savedUser.setRole(Role.WAITER);
        savedUser.setEnabled(true);

        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUser);

        UserEntity result = userService.registerUser("newuser", rawPassword, Role.WAITER);

        assertEquals("newuser", result.getUsername());
        assertEquals(encodedPassword, result.getPassword());
        assertEquals(Role.WAITER, result.getRole());
        assertTrue(result.isEnabled());
    }

    //Test: Username ซ้ำ
    @Test
    void registerUser_shouldThrowException_whenUsernameExists() {
        when(userRepository.existsByUsername("duplicate")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser("duplicate", "1234", Role.MANAGER);
        });
    }
}
