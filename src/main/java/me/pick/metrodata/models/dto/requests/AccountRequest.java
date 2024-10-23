package me.pick.metrodata.models.dto.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountRequest {
  private String username,
      password,
      firstName,
      lastName,
      email,
      phone;

  private Long roleId, instituteId, baseBudget, limitBudget;
}