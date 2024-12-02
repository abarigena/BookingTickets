package ru.abarigena.NauJava.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Контроллер для отображения административной панели.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    /**
     * Возвращает административную панель.
     *
     * @return Шаблон панели администратора.
     */
    @GetMapping
    public String admin() {
        return "admin/adminPanel";
    }
}
