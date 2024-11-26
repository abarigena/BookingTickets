package ru.abarigena.NauJava.Controller.UserController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.abarigena.NauJava.Entities.User.User;
import ru.abarigena.NauJava.Service.UserService.UserService;

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

    @GetMapping("/forgotPassword")
    public String showForgotPasswordForm() {
        return "forgotPassword";
    }

    @PostMapping("/forgotPassword")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        userService.initiatePasswordReset(email);
        return "login";
    }


    @GetMapping("/resetPassword")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "resetPassword";
    }

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
}
