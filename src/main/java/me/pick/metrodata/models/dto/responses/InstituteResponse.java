package me.pick.metrodata.models.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.pick.metrodata.enums.InstituteType;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InstituteResponse {

    private Long id;

    private String instituteName;

    private InstituteType instituteType;
}
