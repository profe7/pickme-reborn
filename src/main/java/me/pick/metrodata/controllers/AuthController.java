package me.pick.metrodata.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import me.pick.metrodata.exceptions.account.AccountDoesNotExistException;
import me.pick.metrodata.exceptions.account.AccountInvalidPasswordException;
import me.pick.metrodata.models.dto.requests.LoginRequest;
import me.pick.metrodata.models.dto.responses.LoginResponse;
import me.pick.metrodata.models.entity.Account;
import me.pick.metrodata.repositories.AccountRepository;
import me.pick.metrodata.services.auth.AuthService;

@Controller
@RequestMapping("/login")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private static final String ACCESS_DENIED_MESSAGE = "accessDeniedMessage";
    private final AccountRepository accountRepository;

    @GetMapping
    public String loginPage(LoginRequest loginRequest, Model model, HttpSession session) {
        String accessDeniedMessage = (String) session.getAttribute(ACCESS_DENIED_MESSAGE);
        if (accessDeniedMessage != null) {
            model.addAttribute(ACCESS_DENIED_MESSAGE, accessDeniedMessage);
            session.removeAttribute(ACCESS_DENIED_MESSAGE);
        }
        return "auth/login";
    }

    @PostMapping
    public String login(LoginRequest loginRequest, HttpSession session) {
        try {
            LoginResponse response = authService.login(loginRequest, session);
        
            Account account = accountRepository.findById(response.getAccountId())
                    .orElseThrow(() -> new AccountDoesNotExistException(response.getUsername()));

            session.setAttribute("name", account.getUser().getFirstName());
            

            return null;
        } catch (AccountDoesNotExistException | AccountInvalidPasswordException e) {
            session.setAttribute("loginErrorMessage", "Username atau password salah.");
            return "redirect:/login?error=true";
        }
    }

    @GetMapping("/forget-password")
    public String forgetPasswordForm(Model model) {
        return "forget-password/forgetPassword";
    }
}
