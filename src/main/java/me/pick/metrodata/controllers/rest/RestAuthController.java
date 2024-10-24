package me.pick.metrodata.controllers.rest;

import me.pick.metrodata.models.dto.requests.ChangePasswordRequest;
import me.pick.metrodata.models.dto.requests.ForgetPasswordRequest;
import me.pick.metrodata.models.dto.requests.LoginRequest;
import me.pick.metrodata.models.dto.responses.ForgotPasswordResponse;
import me.pick.metrodata.models.dto.responses.LoginResponse;
import me.pick.metrodata.services.auth.AuthService;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/auth")
public class RestAuthController {
	private final AuthService authService;

	public RestAuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/login")
	public LoginResponse login(@RequestBody LoginRequest loginRequest) {
		return authService.login(loginRequest);
	}

	@PostMapping("/forget-password")
	public String requestReset(@RequestBody ForgetPasswordRequest forgetPasswordRequest, HttpServletRequest request) {
		Boolean check = authService.requestForget(forgetPasswordRequest.getEmailOrUsername(),
				request.getRequestURL().toString().replace(request.getServletPath(), ""));
		if (check) {
			return "success";
		}
		return "failed";
	}

	@GetMapping("/confirm-reset-password/{token}")
	public ForgotPasswordResponse confirmForget(@PathVariable String token) {
		return authService.validateResetPasswordToken(token);
	}

	@PostMapping("/reset-password/{token}")
	public String changePassword(@PathVariable String token, @RequestBody ChangePasswordRequest changePasswordRequest) {
		if (authService.changePassword(token, changePasswordRequest)) {
			return "success";
		}
		return "failed";
	}
}