package ru.abarigena.NauJava.Service.UserService;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.abarigena.NauJava.Entities.User.User;
import ru.abarigena.NauJava.Entities.User.UserRole;
import ru.abarigena.NauJava.Repository.UserRepository;
import ru.abarigena.NauJava.Service.EmailService.EmailService;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    public void addUser(User user) {
        user.setUserRole(Collections.singleton(UserRole.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setVerificationToken(UUID.randomUUID().toString());
        user.setEmailVerified(false);

        userRepository.save(user);

        // Отправка email подтверждения
        String verificationLink = "http://localhost:8080/verify?token=" + user.getVerificationToken();
        emailService.sendEmail(user.getEmail(), "Подтверждение регистрации",
                "Пройдите по ссылке для подтверждения: " + verificationLink);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * @param token
     * @return
     */
    @Override
    public boolean verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token);
        if(user!= null){
            user.setEmailVerified(true);
            user.setVerificationToken(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public void initiatePasswordReset(String email){
        User user = userRepository.findByEmail(email);
        if(user != null){
            String resetToken = UUID.randomUUID().toString();
            user.setVerificationToken(resetToken);
            userRepository.save(user);

            String resetLink = "http://localhost:8080/resetPassword?token=" + resetToken;
            emailService.sendEmail(user.getEmail(), "Восстановление пароля", "Пройдите по ссылке для сброса пароля: " + resetLink);
        }
    }

    @Transactional
    @Override
    public boolean resetPassword(String token, String newPassword) {
        User user = userRepository.findByVerificationToken(token);
        if(user != null){
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setVerificationToken(null);
            userRepository.save(user);
            return true;
        }
        System.out.println("Ошибка");
        return false;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User myAppUser = userRepository.findByUsername(username);
        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(myAppUser.getUsername(),
                myAppUser.getPassword(), mapRoles(myAppUser.getUserRole()));
        return user;
    }

    private Collection<GrantedAuthority> mapRoles(Set<UserRole> userRoles) {
        return userRoles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.name())).
                collect(Collectors.toList());
    }
}
