package me.pick.metrodata.models.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TalentResponse {

    private String photo;

    private String id;

    private String name;

    private String email;

    private String instituteName;

    private Boolean Disabled;

    private String currentWorkAssigment;

    private Integer salary;
}
