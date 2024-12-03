package me.pick.metrodata.models.dto.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HolidayResponse {
    private int count;
    private List<HolidayDataResponse> data;
}
