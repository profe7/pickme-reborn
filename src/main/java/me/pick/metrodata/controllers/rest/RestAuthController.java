package me.pick.metrodata.controllers.rest;

import lombok.AllArgsConstructor;
import me.pick.metrodata.models.dto.requests.LoginRequest;
import me.pick.metrodata.models.dto.responses.LoginResponse;
import me.pick.metrodata.services.auth.AuthService;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping
public class RestAuthController {
	private AuthService authService;

	@PostMapping ("/login")
	public LoginResponse login (@RequestBody LoginRequest loginRequest) {
		return authService.login (loginRequest);
	}
}