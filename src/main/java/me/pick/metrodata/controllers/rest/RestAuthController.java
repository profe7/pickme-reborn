package me.pick.metrodata.controllers.rest;

import lombok.AllArgsConstructor;
import me.pick.metrodata.models.dto.requests.ChangePasswordRequest;
import me.pick.metrodata.models.dto.requests.ForgetPasswordRequest;
import me.pick.metrodata.models.dto.requests.LoginRequest;
import me.pick.metrodata.models.dto.responses.ForgotPasswordResponse;
import me.pick.metrodata.models.dto.responses.LoginResponse;
import me.pick.metrodata.models.entity.ResetPasswordToken;
import me.pick.metrodata.services.auth.AuthService;
import me.pick.metrodata.utils.Response;
import me.pick.metrodata.utils.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class RestAuthController {
	private final AuthService authService;

	@PostMapping("/login")
	public LoginResponse login(@RequestBody LoginRequest loginRequest) {
		return authService.login(loginRequest);
	}

	@PostMapping("/forget-password")
	public ResponseEntity<Object> requestReset(@RequestBody ForgetPasswordRequest forgetPasswordRequest, HttpServletRequest request) {
		Boolean check = authService.requestForget(forgetPasswordRequest.getEmailOrUsername(),
				request.getRequestURL().toString().replace(request.getServletPath(), ""));

		if (check) {
			return ResponseHandler.generateResponse(new Response(
					"Institute found", HttpStatus.OK, "SUCCESS", null
			));
		} else {
			return ResponseHandler.generateResponse(new Response(
					"Institute not found", HttpStatus.NOT_FOUND, "FAILURE", null
			));
		}
	}

	@GetMapping("/confirm-reset-password/{token}")
	public ResponseEntity<Object> confirmForget(@PathVariable String token) {
		ForgotPasswordResponse forgotPasswordResponse = authService.validateResetPasswordToken(token);
		return ResponseHandler.generateResponse (new Response (
				"Confirm forgot password has success", HttpStatus.OK, "SUCCESS", forgotPasswordResponse
		));
	};

	@PostMapping("/reset-password/{token}")
	public ResponseEntity<Object> changePassword(@PathVariable String token, @RequestBody ChangePasswordRequest changePasswordRequest) {
		if (authService.changePassword(token, changePasswordRequest)) {
			return ResponseHandler.generateResponse(new Response(
					"Confirm forgot password has success", HttpStatus.OK, "SUCCESS", changePasswordRequest
			));
		}
		return ResponseHandler.generateResponse(new Response(
				"Confirm forgot password has failed", HttpStatus.INTERNAL_SERVER_ERROR, "FAILED", changePasswordRequest
		));
	}
}