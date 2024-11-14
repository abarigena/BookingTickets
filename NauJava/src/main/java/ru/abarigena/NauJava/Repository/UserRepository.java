package ru.abarigena.NauJava.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
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
}
