package me.pick.metrodata.models.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HolidayDataResponse {
    private int week;
    private String type;
    private String date;
    private int day;
    private String name;
    private Object info;
    private int mode;
    private String rel;
}
