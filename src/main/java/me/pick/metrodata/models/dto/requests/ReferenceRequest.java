package me.pick.metrodata.models.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReferenceRequest {

    private String reference_name;

    private String reference_group1;

    private Integer reference_group2;
}
