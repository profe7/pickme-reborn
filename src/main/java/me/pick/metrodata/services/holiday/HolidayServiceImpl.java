package me.pick.metrodata.services.holiday;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.pick.metrodata.models.entity.Holiday;
import me.pick.metrodata.repositories.HolidayRepository;

@RequiredArgsConstructor
@Service
public class HolidayServiceImpl implements HolidayService {

    private final HolidayRepository holidayRepository;

    @Override
    public Page<Holiday> getFilteredHoliday(String searchName, LocalDate date, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return holidayRepository.findAllWithFilters(searchName, date, pageable);
    }
}
