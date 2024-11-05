package me.pick.metrodata.models.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.pick.metrodata.models.entity.Role;
import me.pick.metrodata.utils.PageData;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolePaginationResponse {

    private PageData pageData;

    private List<Role> data;

}
