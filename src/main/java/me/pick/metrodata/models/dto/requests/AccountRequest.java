package me.pick.metrodata.models.dto.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountRequest {
  private String accountUsername;

  private String accountPassword;

  private String accountFirstName;

  private String accountLastName;

  private String accountEmail;

  private Long roleId;

  private Long instituteId;

  public AccountRequest(String accountUsername, String accountPassword, String accountFirstName, String accountLastName, String accountEmail, Long roleId, Long instituteId) {
    this.accountUsername = accountUsername;
    this.accountPassword = accountPassword;
    this.accountFirstName = accountFirstName;
    this.accountLastName = accountLastName;
    this.accountEmail = accountEmail;
    this.roleId = roleId;
    this.instituteId = instituteId;
  }
}