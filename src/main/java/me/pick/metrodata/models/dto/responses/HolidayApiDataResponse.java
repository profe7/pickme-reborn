package me.pick.metrodata.models.dto.responses;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HolidayApiDataResponse {
    private int type;
    private int year;
    private String name;
    private String initial;
    private Map<String, HolidayResponse> holiday;
    private Map<String, HolidayResponse> leave;
    private Map<String, HolidayResponse> islamic;
    private Map<String, HolidayResponse> longWeekend;
    private Map<String, HolidayResponse> harpitnas;
}
