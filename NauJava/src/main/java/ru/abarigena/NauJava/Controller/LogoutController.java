package ru.abarigena.NauJava.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер для управления процессом выхода из системы.
 */
@Controller
public class LogoutController {

    /**
     * Выполняет выход пользователя из системы.
     *
     * @param request  Объект HTTP-запроса.
     * @param response Объект HTTP-ответа.
     * @return Перенаправление на страницу входа.
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, null);
        return "redirect:/login";
    }
}
