package me.pick.metrodata.controllers;

import lombok.AllArgsConstructor;
import me.pick.metrodata.exceptions.account.AccountDoesNotExistException;
import me.pick.metrodata.exceptions.account.AccountInvalidPasswordException;
import me.pick.metrodata.models.dto.requests.LoginRequest;
import me.pick.metrodata.services.auth.AuthService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    private static final String ACCESS_DENIED_MESSAGE = "accessDeniedMessage";

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
            authService.login(loginRequest, session);

            if (hasAuthority("UPDATE_APPLICANT_NOMINEE") && hasAuthority("READ_PARAMETER")) {
                return "redirect:/talent-mitra/add";
            } else if (hasAuthority("CREATE_APPLICANT_NOMINEE")) {
                return "redirect:/mitra";
            } else if (hasAuthority("CREATE_ROLE")) {
                return "redirect:/admin";
            } else {
                return "redirect:/";
            }
        } catch (AccountDoesNotExistException | AccountInvalidPasswordException e) {
            session.setAttribute("loginErrorMessage", "Username atau password salah.");
            return "redirect:/login?error=true";
        }
    }

    private boolean hasAuthority(String authority) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(authority));
    }
}
