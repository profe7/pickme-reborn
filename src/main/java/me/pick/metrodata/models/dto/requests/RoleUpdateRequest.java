package me.pick.metrodata.models.dto.requests;

import lombok.Data;

import java.util.List;

@Data
public class RoleUpdateRequest {
    private Long id;
    private String name;
    private List<Long> privilegeIds;
}
