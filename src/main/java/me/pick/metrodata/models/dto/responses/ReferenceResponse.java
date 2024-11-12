package me.pick.metrodata.models.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReferenceResponse {

    private Long id;

    private String reference_name;

    private String reference_group1;

    private Integer reference_group2;
}
