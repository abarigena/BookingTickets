package ru.abarigena.NauJava.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.abarigena.NauJava.Entities.User.User;
import ru.abarigena.NauJava.Entities.User.UserRole;

@RepositoryRestResource
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Возвращает объект {@link User} по его имени пользователя.
     *
     * @param username имя пользователя, для которого нужно найти запись
     * @return объект {@link User}, соответствующий указанному имени пользователя
     */
    User findByUsername(String username);

    /**
     * Запрос для подсчета количества пользователей по их роли.
     *
     * @param userRole роль пользователя, по которой необходимо выполнить подсчет.
     * @return количество пользователей с заданной ролью.
     */
    @Query("select count(u) from User u join u.userRole r where r = :userRole")
    Long countByUserRoles(UserRole userRole);

    /**
     * Находит пользователя по токену верификации.
     *
     * @param token токен верификации
     * @return объект {@link User}, соответствующий токену
     */
    User findByVerificationToken(String token);

    /**
     * Находит пользователя по адресу электронной почты.
     *
     * @param email адрес электронной почты
     * @return объект {@link User}, соответствующий email
     */
    User findByEmail(String email);

    User findByPhoneNumber(String phoneNumber);

    @Query("SELECT u FROM User u WHERE u.username = :identifier OR u.email = :identifier OR u.phoneNumber = :identifier")
    User findByUsernameOrEmailOrPhone(@Param("identifier") String identifier);
}
