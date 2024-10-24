package me.pick.metrodata.controllers.rest;

import me.pick.metrodata.models.dto.requests.LoginRequest;
import me.pick.metrodata.models.dto.responses.LoginResponse;
import me.pick.metrodata.services.auth.AuthService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping
public class RestAuthController {
	private final AuthService authService;

	public RestAuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping ("/login")
	public LoginResponse login (@RequestBody LoginRequest loginRequest) {
		return authService.login (loginRequest);
	}
}