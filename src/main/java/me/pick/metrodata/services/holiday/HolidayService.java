package me.pick.metrodata.services.holiday;

import java.time.LocalDate;

import org.springframework.data.domain.Page;

import me.pick.metrodata.models.entity.Holiday;

public interface HolidayService {

    Page<Holiday> getFilteredHoliday(String searchName, LocalDate date, Integer page, Integer size);

    Holiday getHolidayById(Long id);
}
