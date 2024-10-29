package me.pick.metrodata.controllers;

import lombok.AllArgsConstructor;
import me.pick.metrodata.exceptions.account.AccountDoesNotExistException;
import me.pick.metrodata.exceptions.account.AccountInvalidPasswordException;
import me.pick.metrodata.models.dto.requests.LoginRequest;
import me.pick.metrodata.models.dto.responses.LoginResponse;
import me.pick.metrodata.services.auth.AuthService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping ("/login")
@AllArgsConstructor
public class AuthController {
	private AuthService authService;

	@GetMapping
	public String loginPage(LoginRequest loginRequest, Model model, HttpSession session) {
		String accessDeniedMessage = (String) session.getAttribute("accessDeniedMessage");
		if (accessDeniedMessage != null) {
			model.addAttribute("accessDeniedMessage", accessDeniedMessage);
			session.removeAttribute("accessDeniedMessage");
		}
		return "auth/login";
	}

	@PostMapping
	public String login(LoginRequest loginRequest, HttpSession session) {
		try {
			LoginResponse loginResponse = authService.login(loginRequest);

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			List<String> authorities = authentication.getAuthorities().stream()
					.map(auth -> auth.getAuthority())
					.collect(Collectors.toList());

			if (authorities.contains("UPDATE_APPLICANT_NOMINEE") && authorities.contains("READ_PARAMETER")) {
				return "redirect:/talent-mitra/add";
			} else if (authorities.contains("CREATE_APPLICANT_NOMINEE")) {
				return "redirect:/mitra";
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
