package ru.abarigena.NauJava.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Контроллер для управления процессом входа в систему.
 */
@Controller
public class LoginController {

    /**
     * Отображает страницу входа.
     *
     * @param logout Опциональный параметр, указывающий, была ли выполнена операция выхода.
     * @param model  Модель для передачи данных в представление.
     * @return Шаблон страницы входа.
     */
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "logout", required = false) String logout, Model model) {
        if (logout != null) {
            model.addAttribute("logout", true);
        }
        return "login";
    }

}
