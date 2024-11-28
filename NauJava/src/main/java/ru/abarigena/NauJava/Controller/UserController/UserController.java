package ru.abarigena.NauJava.Controller.UserController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.abarigena.NauJava.Entities.User.User;
import ru.abarigena.NauJava.Service.UserService.UserService;

import java.security.Principal;

/**
 * Контроллер для обработки запросов, связанных с регистрацией пользователей.
 * Управляет отображением страницы регистрации и обработкой регистрации нового пользователя.
 */
@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    /**
     * Показать страницу регистрации нового пользователя.
     *
     * @return путь к шаблону страницы регистрации
     */
    @GetMapping("/registration")
    public String getRegistration(){
        return "registration";
    }

    /**
     * Обрабатывает POST-запрос для регистрации нового пользователя.
     * Если пользователь успешно зарегистрирован, происходит перенаправление на страницу авторизации.
     * В случае ошибки (например, если пользователь уже существует), возвращает страницу регистрации с сообщением об ошибке.
     *
     * @param user  Объект пользователя, который регистрируется
     * @param model Модель для передачи данных в представление
     * @return имя представления или перенаправление в случае успешной регистрации
     */
    @PostMapping("/registration")
    public String registrerUser(User user, Model model){
        try{
            userService.addUser(user);
            return "redirect:/login";
        }
        catch(Exception e){
            model.addAttribute("message", "User exists");
            return "registration";
        }
    }

    /**
     * Подтверждение email-адреса пользователя с использованием токена.
     *
     * @param token Токен подтверждения, передаваемый через URL
     * @param model Модель для передачи данных в представление
     * @return путь к шаблону с подтверждением или ошибкой
     */
    @GetMapping("/verify")
    public String verifyEmail(@RequestParam("token") String token, Model model){
        if(userService.verifyEmail(token)){
            model.addAttribute("message", "Email успешно подтвержден!");
            return "redirect:/bookTicket";
        }else {
            model.addAttribute("message", "Неверный или истекший токен!");
            return "error";
        }
    }

    /**
     * Отображение формы для ввода email для восстановления пароля.
     *
     * @return путь к шаблону страницы восстановления пароля
     */
    @GetMapping("/forgotPassword")
    public String showForgotPasswordForm() {
        return "forgotPassword";
    }

    /**
     * Обработка запроса на восстановление пароля по email.
     *
     * @param email Адрес электронной почты пользователя
     * @param model Модель для передачи данных в представление
     * @return путь к шаблону страницы входа
     */
    @PostMapping("/forgotPassword")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        userService.initiatePasswordReset(email);
        return "login";
    }

    /**
     * Показать форму для сброса пароля с использованием токена.
     *
     * @param token Токен для сброса пароля
     * @param model Модель для передачи данных в представление
     * @return путь к шаблону для сброса пароля
     */
    @GetMapping("/resetPassword")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "resetPassword";
    }

    /**
     * Обработка сброса пароля с использованием токена и нового пароля.
     *
     * @param token Токен для сброса пароля
     * @param newPassword Новый пароль пользователя
     * @param model Модель для передачи данных в представление
     * @return путь к шаблону входа или ошибке
     */
    @PostMapping("/resetPassword")
    public String processResetPassword(@RequestParam("token") String token,
                                       @RequestParam("password") String newPassword,
                                       Model model) {
        if (userService.resetPassword(token, newPassword)) {
            model.addAttribute("message", "Password reset successfully!");
            return "login";
        } else {
            model.addAttribute("message", "Invalid or expired token!");
            return "error";
        }
    }

    /**
     * Показать страницу профиля текущего пользователя.
     *
     * @param model     Модель для передачи данных в представление
     * @param principal Объект для получения информации о текущем пользователе
     * @return путь к шаблону профиля пользователя
     */
    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);
        return "userView/profile";
    }

    /**
     * Обновление данных профиля пользователя.
     *
     * @param updatedUser Обновленные данные пользователя
     * @param principal   Объект для получения информации о текущем пользователе
     * @return перенаправление на страницу профиля
     */
    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute("user") User updatedUser, Principal principal) {
        String username = principal.getName();

        userService.updateUser(username,updatedUser.getFirstName(),updatedUser.getLastName(),
                updatedUser.getAge(),updatedUser.getPhoneNumber());
        return "redirect:/profile";
    }
}
