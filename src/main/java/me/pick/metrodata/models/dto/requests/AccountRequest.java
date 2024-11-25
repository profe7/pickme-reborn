package me.pick.metrodata.models.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest {

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private Long baseBudget;

    private Long limitBudget;

    private Long roleId;

    private Long instituteId;

}
