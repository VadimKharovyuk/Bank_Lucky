package com.example.bank_lucky.controller;
import com.example.bank_lucky.dto.ClientDto;
import com.example.bank_lucky.request.LoginRequest;
import com.example.bank_lucky.service.CardClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
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
    public String loginClient(LoginRequest loginRequest, Model model) {
        try {
            String message = cardClientService.loginClient(loginRequest);
            model.addAttribute("successMessage", message);
            return "HomePage"; // Перенаправление на главную страницу после успешного логина
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "login"; // Возврат на страницу логина в случае ошибки
        }
    }
}