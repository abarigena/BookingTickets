package ru.abarigena.NauJava.Service.UserService;

import jakarta.transaction.Transactional;
import org.slf4j.LoggerFactory;
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

import java.util.*;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

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
        logger.info("Добавление пользователя: {}", user);

        try{
            user.setUserRole(Collections.singleton(UserRole.USER));
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            user.setVerificationToken(UUID.randomUUID().toString());
            user.setEmailVerified(false);

            userRepository.save(user);

            // Отправка email подтверждения
            String verificationLink = "http://localhost:8080/verify?token=" + user.getVerificationToken();
            CompletableFuture.runAsync(() -> {
                emailService.sendEmail(user.getEmail(), "Подтверждение регистрации",
                        "Пройдите по ссылке для подтверждения: " + verificationLink);
                logger.info("Письмо для подтверждения регистрации отправлено на email: {}", user.getEmail());
            });
        } catch (Exception e) {
            logger.error("Ошибка при добавлении пользователя: {}", user.getUsername(), e);
        }
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
        logger.info("Подтверждение email с токеном: {}", token);

        User user = userRepository.findByVerificationToken(token);
        if(user!= null){
            user.setEmailVerified(true);
            user.setVerificationToken(null);
            userRepository.save(user);
            logger.info("Email успешно подтвержден для пользователя: {}", user.getUsername());
            return true;
        }
        logger.warn("Не удалось подтвердить email: пользователь с токеном {} не найден", token);
        return false;
    }

    /**
     * Инициирует процесс восстановления пароля, генерирует токен сброса и отправляет email со ссылкой.
     *
     * @param email email пользователя
     */
    @Override
    public void initiatePasswordReset(String email){
        logger.info("Инициация восстановления пароля для email: {}", email);
        User user = userRepository.findByEmail(email);
        if(user != null){
            String resetToken = UUID.randomUUID().toString();
            user.setVerificationToken(resetToken);
            userRepository.save(user);

            String resetLink = "http://localhost:8080/resetPassword?token=" + resetToken;
            CompletableFuture.runAsync(()->{
                emailService.sendEmail(user.getEmail(), "Восстановление пароля", "Пройдите по ссылке для сброса пароля: "
                        + resetLink);
                logger.info("Письмо для восстановления пароля отправлено на email: {}", user.getEmail());
            });
        }else {
            logger.warn("Не удалось найти пользователя для восстановления пароля по email: {}", email);
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
        logger.info("Сброс пароля для пользователя с токеном: {}", token);
        User user = userRepository.findByVerificationToken(token);
        if(user != null){
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setVerificationToken(null);
            userRepository.save(user);
            logger.info("Пароль успешно сброшен для пользователя: {}", user.getUsername());
            return true;
        }
        logger.error("Не удалось сбросить пароль: пользователь с токеном {} не найден", token);
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
        logger.info("Обновление информации для пользователя: {}", username);

        User user = userRepository.findByUsername(username);

        if(user != null){
            user.setFirstName(firstName);
            user.setLastName(lastname);
            user.setAge(age);
            user.setPhoneNumber(phoneNumber);

            userRepository.save(user);
            logger.info("Информация о пользователе обновлена: {}", username);
        }else {
            logger.warn("Не удалось найти пользователя с именем: {}", username);
        }
        return user;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    /**
     * @return
     */
    @Override
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
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
        logger.info("Загрузка данных пользователя для аутентификации: {}", username);
        User myAppUser = userRepository.findByUsername(username);
        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(myAppUser.getUsername(),
                myAppUser.getPassword(), mapRoles(myAppUser.getUserRole()));
        logger.info("Пользователь {} найден и готов для аутентификации", username);
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
