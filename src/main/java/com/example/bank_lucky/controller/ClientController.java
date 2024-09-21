package com.example.bank_lucky.controller;
import com.example.bank_lucky.dto.ClientDto;
import com.example.bank_lucky.request.LoginRequest;
import com.example.bank_lucky.service.CardClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ClientController {

    private final CardClientService cardClientService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("clientDto", new ClientDto());
        return "register"; // Имя HTML-шаблона для регистрации
    }

    @PostMapping("/register")
    public String registerClient(ClientDto clientDto, Model model) {
        try {
            String message = cardClientService.registerClient(clientDto);
            model.addAttribute("successMessage", message);
            return "login"; // Перенаправление на страницу логина после успешной регистрации
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register"; // Возврат на страницу регистрации в случае ошибки
        }
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login"; // Имя HTML-шаблона для логина
    }

    @PostMapping("/login")
    public String loginClient(@ModelAttribute LoginRequest loginRequest, Model model) {
        log.info("Login attempt for email: {}", loginRequest.getEmail());
        try {
            String message = cardClientService.loginClient(loginRequest);
            model.addAttribute("successMessage", message);
            log.info("Login successful for user: {}", loginRequest.getEmail());
            return "HomePage";
        } catch (Exception e) {
            log.error("Login failed for user: {}. Error: {}", loginRequest.getEmail(), e.getMessage(), e);
            model.addAttribute("errorMessage", "Ошибка аутентификации: " + e.getMessage());
            return "login";
        }
    }
    @GetMapping("/dashboard")
    public String showDashboard(Model model, Principal principal) {
        // Получите информацию о клиенте по email (или любому другому уникальному идентификатору)
        String email = principal.getName(); // Предполагается, что клиент авторизован
        ClientDto clientDto = cardClientService.getClientByEmail(email);
        model.addAttribute("client", clientDto);
        return "dashboard"; // Имя HTML-шаблона для личного аккаунта
    }
}