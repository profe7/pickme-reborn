package me.pick.metrodata.models.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.pick.metrodata.models.entity.Institute;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TalentResponse {

    private String id;

    private String nik;

    private String name;

    private String email;

    private String currentWorkAssignment;

    private String phone;

    private String photo;

    private String userUrl;

    private String updateAt;

    private List<String> skill;

    private Boolean disabled;

    private Boolean invited;

    private Institute institute;

}
