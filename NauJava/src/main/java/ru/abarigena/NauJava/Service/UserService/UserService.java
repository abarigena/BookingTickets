package ru.abarigena.NauJava.Service.UserService;

import ru.abarigena.NauJava.Entities.User.User;

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

    boolean verifyEmail(String token);

    void initiatePasswordReset(String email);

    boolean resetPassword(String token, String newPassword);

    User updateUser(String username, String firstName, String lastname, int age, String phoneNumber);
}
