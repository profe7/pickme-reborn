package me.pick.metrodata.models.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest {

    private String accountUsername;

    private String accountPassword;

    private String accountFirstName;

    private String accountLastName;

    private String accountEmail;

    private Long roleId;

    private Long instituteId;

}
