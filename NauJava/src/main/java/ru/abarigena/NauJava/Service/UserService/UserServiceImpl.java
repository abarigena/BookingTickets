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
import java.util.concurrent.CompletableFuture;
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

    /**
     * Добавляет нового пользователя, устанавливает роль, шифрует пароль,
     * генерирует токен подтверждения и отправляет email для подтверждения регистрации.
     *
     * @param user новый пользователь
     */
    @Override
    public void addUser(User user) {
        user.setUserRole(Collections.singleton(UserRole.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setVerificationToken(UUID.randomUUID().toString());
        user.setEmailVerified(false);

        userRepository.save(user);

        // Отправка email подтверждения
        String verificationLink = "http://localhost:8080/verify?token=" + user.getVerificationToken();
        CompletableFuture.runAsync(() ->{
            emailService.sendEmail(user.getEmail(), "Подтверждение регистрации",
                    "Пройдите по ссылке для подтверждения: " + verificationLink);
        });
    }

    /**
     * Ищет пользователя по имени.
     *
     * @param username имя пользователя
     * @return найденный пользователь или null, если пользователь не найден
     */
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Подтверждает email пользователя с помощью токена.
     *
     * @param token токен подтверждения
     * @return true, если email подтвержден успешно, иначе false
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

    /**
     * Инициирует процесс восстановления пароля, генерирует токен сброса и отправляет email со ссылкой.
     *
     * @param email email пользователя
     */
    @Override
    public void initiatePasswordReset(String email){
        User user = userRepository.findByEmail(email);
        if(user != null){
            String resetToken = UUID.randomUUID().toString();
            user.setVerificationToken(resetToken);
            userRepository.save(user);

            String resetLink = "http://localhost:8080/resetPassword?token=" + resetToken;
            CompletableFuture.runAsync(()->{
                emailService.sendEmail(user.getEmail(), "Восстановление пароля", "Пройдите по ссылке для сброса пароля: " + resetLink);
            });
        }
    }

    /**
     * Сбрасывает пароль пользователя, используя токен сброса.
     *
     * @param token       токен сброса пароля
     * @param newPassword новый пароль
     * @return true, если пароль успешно сброшен, иначе false
     */
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

    /**
     * Обновляет информацию о пользователе.
     *
     * @param username    имя пользователя
     * @param firstName   новое имя
     * @param lastname    новая фамилия
     * @param age         новый возраст
     * @param phoneNumber новый номер телефона
     * @return обновленный пользователь
     */
    @Override
    public User updateUser(String username, String firstName, String lastname, int age, String phoneNumber) {
        User user = userRepository.findByUsername(username);

        user.setFirstName(firstName);
        user.setLastName(lastname);
        user.setAge(age);
        user.setPhoneNumber(phoneNumber);

        userRepository.save(user);
        return user;
    }

    /**
     * Загружает данные пользователя для аутентификации.
     *
     * @param username имя пользователя
     * @return объект UserDetails для Spring Security
     * @throws UsernameNotFoundException если пользователь не найден
     */
    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User myAppUser = userRepository.findByUsername(username);
        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(myAppUser.getUsername(),
                myAppUser.getPassword(), mapRoles(myAppUser.getUserRole()));
        return user;
    }

    /**
     * Преобразует роли пользователя в объекты GrantedAuthority для Spring Security.
     *
     * @param userRoles набор ролей пользователя
     * @return коллекция GrantedAuthority
     */
    private Collection<GrantedAuthority> mapRoles(Set<UserRole> userRoles) {
        return userRoles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.name())).
                collect(Collectors.toList());
    }
}
