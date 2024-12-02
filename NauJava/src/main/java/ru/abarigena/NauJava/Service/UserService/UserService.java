package ru.abarigena.NauJava.Service.UserService;

import ru.abarigena.NauJava.Entities.User.User;

import java.util.List;

/**
 * Сервис для управления объектами сущности {@link User}.
 */
public interface UserService {

    /**
     * Добавляет нового пользователя в систему.
     *
     * @param user объект {@link User}, который нужно добавить
     */
    void addUser(User user);

    /**
     * Находит пользователя по имени пользователя.
     *
     * @param username имя пользователя, по которому необходимо выполнить поиск
     * @return объект {@link User}, соответствующий указанному имени пользователя
     */
    User findByUsername(String username);

    /**
     * Проверяет email пользователя с использованием токена подтверждения.
     *
     * @param token токен подтверждения
     * @return true, если подтверждение успешно; иначе false
     */
    boolean verifyEmail(String token);

    /**
     * Инициирует процесс сброса пароля для пользователя с указанным email.
     *
     * @param email адрес электронной почты пользователя
     */
    void initiatePasswordReset(String email);

    /**
     * Сбрасывает пароль пользователя, используя токен подтверждения.
     *
     * @param token токен подтверждения
     * @param newPassword новый пароль
     * @return true, если сброс пароля успешен; иначе false
     */
    boolean resetPassword(String token, String newPassword);

    /**
     * Обновляет данные пользователя.
     *
     * @param username имя пользователя
     * @param firstName новое имя
     * @param lastname новая фамилия
     * @param age новый возраст
     * @param phoneNumber новый номер телефона
     * @return обновленный объект {@link User}
     */
    User updateUser(String username, String firstName, String lastname, int age, String phoneNumber);

    /**
     * Находит пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     * @return объект {@link User}, соответствующий указанному идентификатору
     */
    User findById(Long id);

    /**
     * Возвращает список всех пользователей.
     *
     * @return список всех пользователей
     */
    List<User> findAll();
}
