package me.pick.metrodata.models.dto.requests;

import lombok.Data;

@Data
public class ChangePasswordRequest {
	private String newPassword;
	private String confirmNewPassword;
}
