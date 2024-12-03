package me.pick.metrodata.services.holiday;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import me.pick.metrodata.models.dto.requests.HolidayRequest;
import me.pick.metrodata.models.dto.responses.HolidayApiResponse;
import me.pick.metrodata.models.entity.Holiday;

public interface HolidayService {

    Page<Holiday> getFilteredHoliday(String searchName, LocalDate date, Integer page, Integer size);

    Holiday getHolidayById(Long id);

    void create(HolidayRequest holidayRequest);

    void update(Long id, HolidayRequest holidayRequest);

    void delete(Long id);

    HolidayApiResponse readJSONFile(String fileName);

    Boolean uploadJSONFile(MultipartFile file);
}
