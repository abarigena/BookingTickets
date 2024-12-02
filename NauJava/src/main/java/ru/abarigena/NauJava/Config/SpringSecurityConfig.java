package ru.abarigena.NauJava.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Конфигурационный класс для настройки безопасности Spring Security.
 * Аннотация {@link EnableWebSecurity} включает поддержку веб-безопасности в приложении.
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    /**
     * Создает бин для кодировщика паролей, использующего алгоритм BCrypt.
     *
     * @return экземпляр {@link Pbkdf2PasswordEncoder}, который используется для шифрования паролей.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Pbkdf2PasswordEncoder("secret-key", 8, 65536, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
    }

    /**
     * Настраивает фильтр безопасности, определяющий, какие URL-адреса требуют аутентификации и какие доступны без нее.
     *
     * @param httpSecurity объект {@link HttpSecurity}, с помощью которого конфигурируется безопасность HTTP
     * @return настроенная цепочка фильтров безопасности
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/registration", "/forgotPassword", "/resetPassword", "/verify").permitAll()
                        .requestMatchers("/swagger-ui/index.html","/admin/**","/swagger-ui/**",
                                "/v3/api-docs/**","/api/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .csrf(crsf -> crsf.disable())
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/bookTicket",true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login") // Перенаправление после выхода
                        .invalidateHttpSession(true) // Удаление сессии
                        .deleteCookies("JSESSIONID") // Удаление cookies
                )
                .exceptionHandling(exceptions -> exceptions
                        .accessDeniedPage("/403"));

        return httpSecurity.build();
    }

}
