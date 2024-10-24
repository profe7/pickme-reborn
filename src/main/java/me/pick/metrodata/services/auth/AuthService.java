package me.pick.metrodata.services.auth;

import me.pick.metrodata.models.entity.Account;
import me.pick.metrodata.models.dto.requests.ChangePasswordRequest;
import me.pick.metrodata.models.dto.requests.LoginRequest;
import me.pick.metrodata.models.dto.responses.ForgotPasswordResponse;
import me.pick.metrodata.models.dto.responses.LoginResponse;

public interface AuthService {

	LoginResponse login (LoginRequest loginRequest);

	Account getLoggedAccountData ();

	ForgotPasswordResponse validateResetPasswordToken (String token);

	Boolean changePassword (String token, ChangePasswordRequest changePasswordRequest);
}
