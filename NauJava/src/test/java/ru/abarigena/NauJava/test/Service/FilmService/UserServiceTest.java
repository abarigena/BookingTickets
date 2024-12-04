package ru.abarigena.NauJava.test.Service.FilmService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.abarigena.NauJava.Entities.User.User;
import ru.abarigena.NauJava.Repository.UserRepository;
import ru.abarigena.NauJava.Service.EmailService.EmailService;
import ru.abarigena.NauJava.Service.UserService.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest  {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testUser");
        user.setPassword("password");
        user.setEmail("test@example.com");
    }

    @Test
    void testAddUser() {
        user.setPassword("password");
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.addUser(user);

        verify(userRepository, times(1)).save(user);
        assertEquals("encodedPassword", user.getPassword());
        assertNotNull(user.getVerificationToken());
        assertFalse(user.isEmailVerified());
        verify(emailService, times(1)).sendEmail(eq(user.getEmail()), anyString(), anyString());
    }

    @Test
    void testVerifyEmail_Success() {
        user.setVerificationToken("validToken");
        when(userRepository.findByVerificationToken("validToken")).thenReturn(user);

        boolean result = userService.verifyEmail("validToken");

        assertTrue(result);
        assertTrue(user.isEmailVerified());
        assertNull(user.getVerificationToken());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testVerifyEmail_Failure() {
        boolean result = userService.verifyEmail("invalidToken");

        assertFalse(result);
    }

    @Test
    void testResetPassword_Success() {
        user.setVerificationToken("resetToken");
        when(userRepository.findByVerificationToken("resetToken")).thenReturn(user);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        boolean result = userService.resetPassword("resetToken", "newPassword");

        assertTrue(result);
        assertEquals("encodedNewPassword", user.getPassword());
        assertNull(user.getVerificationToken());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testResetPassword_Failure() {
        boolean result = userService.resetPassword("invalidToken", "newPassword");

        assertFalse(result);
    }

    @Test
    void testUpdateUser_Success() {
        user.setFirstName("OldName");
        when(userRepository.findByUsername("testUser")).thenReturn(user);

        User updatedUser = userService.updateUser("testUser", "NewName", "NewLastName", 30, "123456789");

        assertEquals("NewName", updatedUser.getFirstName());
        assertEquals("NewLastName", updatedUser.getLastName());
        assertEquals(30, updatedUser.getAge());
        assertEquals("123456789", updatedUser.getPhoneNumber());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUser_Failure() {
        when(userRepository.findByUsername("testUser")).thenReturn(null);

        User updatedUser = userService.updateUser("testUser", "NewName", "NewLastName", 30, "123456789");

        assertNull(updatedUser);
    }

    @Test
    void testLoadUserByUsername() {
        user.setPassword("password");
        when(userRepository.findByUsername("testUser")).thenReturn(user);

        UserDetails userDetails = userService.loadUserByUsername("testUser");

        assertEquals("testUser", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
    }

}
