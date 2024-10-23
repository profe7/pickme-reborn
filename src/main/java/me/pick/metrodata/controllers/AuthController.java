package me.pick.metrodata.controllers;

import lombok.AllArgsConstructor;
import me.pick.metrodata.models.entity.Account;
import me.pick.metrodata.models.dto.requests.AccountRequest;
import me.pick.metrodata.models.dto.requests.LoginRequest;
import me.pick.metrodata.models.dto.responses.LoginResponse;
import me.pick.metrodata.services.auth.AuthService;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping
public class AuthController {
	private AuthService authService;

	@PostMapping ("/login")
	public LoginResponse login (@RequestBody LoginRequest loginRequest) {
		return authService.login (loginRequest);
	}
}