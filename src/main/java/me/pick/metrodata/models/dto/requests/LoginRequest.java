package me.pick.metrodata.models.dto.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRequest {
  private String username;
  private String password;
}