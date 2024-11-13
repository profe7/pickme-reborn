package me.pick.metrodata.models.dto.requests;

import lombok.Data;

@Data
public class InstituteUpdateRequest {
    private String name;

    private String instituteType;
}
