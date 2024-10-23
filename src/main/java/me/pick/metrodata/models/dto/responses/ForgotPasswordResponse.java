package me.pick.metrodata.models.dto.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ForgotPasswordResponse {
	public Long accountId;
	public String email;
	@JsonFormat (pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	public LocalDateTime updatedAt;
}
