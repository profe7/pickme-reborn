package me.pick.metrodata.services.auth;

import lombok.RequiredArgsConstructor;
import me.pick.metrodata.exceptions.account.AccountDoesNotExistException;
import me.pick.metrodata.exceptions.account.AccountInvalidPasswordException;
import me.pick.metrodata.models.dto.requests.ChangePasswordRequest;
import me.pick.metrodata.models.dto.requests.LoginRequest;
import me.pick.metrodata.models.dto.responses.ForgotPasswordResponse;
import me.pick.metrodata.models.dto.responses.LoginResponse;
import me.pick.metrodata.models.entity.*;
import me.pick.metrodata.repositories.AccountRepository;
import me.pick.metrodata.repositories.ResetPasswordTokenRepository;
import me.pick.metrodata.services.accountdetail.AccountDetailService;
import me.pick.metrodata.utils.AuthUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private final AccountRepository accountRepository;
	private final ResetPasswordTokenRepository resetPasswordTokenRepository;
	private final AuthenticationManager authenticationManager;
	private final AccountDetailService accountDetailService;
	private final PasswordEncoder passwordEncoder;


	public LoginResponse login (LoginRequest loginRequest) {
		UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken (loginRequest.getUsername (), loginRequest.getPassword ());

		Account account = accountRepository.findByUsernameOrUserEmail (loginRequest.getUsername (), loginRequest.getUsername ()).orElseThrow (() -> new AccountDoesNotExistException(loginRequest.getUsername()));

		try {
			Authentication auth = authenticationManager.authenticate (authReq);
			SecurityContextHolder.getContext ().setAuthentication (auth);
		} catch (AuthenticationException e) {
			throw new AccountInvalidPasswordException();
		}

		UserDetails userDetails = accountDetailService.loadUserByUsername (loginRequest.getUsername ());

		List<String> authorities = userDetails.getAuthorities ().stream ().map (authority -> authority.getAuthority ()).collect (Collectors.toList ());

		return new LoginResponse (account.getUsername (), authorities);
	}

	public Account getLoggedAccountData () {
		Long id = AuthUtil.getLoginUserId ();
		return accountRepository.findById (id).orElse (null);
	}

	public ForgotPasswordResponse validateResetPasswordToken (String token) {
		if ( token == null ) {
			throw new ResponseStatusException (HttpStatus.NOT_FOUND, "Token is null");
		} else {

			ResetPasswordToken resetPasswordToken = resetPasswordTokenRepository.findByToken (token).orElseThrow (() -> new ResponseStatusException (HttpStatus.NOT_FOUND, "Token is not found"));

			if ( resetPasswordToken.getToken () == null ) {
				throw new ResponseStatusException (HttpStatus.NOT_FOUND, "Token not valid");
			}

			if ( LocalDateTime.now ().isAfter (resetPasswordToken.getExpiryDateTime ()) ) {
				throw new ResponseStatusException (HttpStatus.FORBIDDEN, "Token has expired");
			}

			ForgotPasswordResponse response = new ForgotPasswordResponse ();
			response.setAccountId (resetPasswordToken.getAccount ().getId ());
			response.setEmail (resetPasswordToken.getAccount ().getUser ().getEmail ());
			response.setUpdatedAt (resetPasswordToken.getAccount ().getUpdatedAt ());
			return response;
		}
	}

	public Boolean changePassword (String token, ChangePasswordRequest changePasswordRequest) {
		ResetPasswordToken resetPasswordToken = resetPasswordTokenRepository.findByToken (token).orElseThrow (() -> new ResponseStatusException (HttpStatus.NOT_FOUND, "Token tidak valid"));

		if ( changePasswordRequest.getNewPassword () == null || changePasswordRequest.getConfirmNewPassword () == null ) {
			throw new ResponseStatusException (HttpStatus.BAD_REQUEST, "New password and confirm new password cannot be null");
		}

		if ( ! changePasswordRequest.getNewPassword ().equals (changePasswordRequest.getConfirmNewPassword ()) ) {
			throw new ResponseStatusException (HttpStatus.BAD_REQUEST, "New Password and confirm new password must same");
		}

		if ( LocalDateTime.now ().isAfter (resetPasswordToken.getExpiryDateTime ()) ) {
			throw new ResponseStatusException (HttpStatus.FORBIDDEN, "Token has expired");
		}

		Account account = resetPasswordToken.getAccount ();

		if ( account == null ) {
			throw new ResponseStatusException (HttpStatus.BAD_REQUEST, "Account not found");
		}

		account.setPassword (passwordEncoder.encode (changePasswordRequest.getConfirmNewPassword ()));
		accountRepository.save (account);
		resetPasswordTokenRepository.delete (resetPasswordToken);

		return true;
	}
}
