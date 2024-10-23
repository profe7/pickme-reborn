package me.pick.metrodata.models.dto.requests;

import lombok.Data;

@Data
public class ForgetPasswordRequest {
  private String emailOrUsername;
} 
