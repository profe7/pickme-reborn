package me.pick.metrodata.services.holiday;

import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.pick.metrodata.models.dto.requests.HolidayRequest;
import me.pick.metrodata.models.entity.Holiday;
import me.pick.metrodata.repositories.HolidayRepository;
import me.pick.metrodata.utils.DateTimeUtil;

@RequiredArgsConstructor
@Service
public class HolidayServiceImpl implements HolidayService {

    private final HolidayRepository holidayRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<Holiday> getFilteredHoliday(String searchName, LocalDate date, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return holidayRepository.findAllWithFilters(searchName, date, pageable);
    }

    @Override
    public Holiday getHolidayById(Long id) {
        return holidayRepository.findById(id).orElseThrow();
    }

    @Override
    public void create(HolidayRequest holidayRequest) {
        Holiday holiday = modelMapper.map(holidayRequest, Holiday.class);
        holiday.setDate(DateTimeUtil.stringToLocalDate(holidayRequest.getDate()));

        holidayRepository.save(holiday);
    }

    @Override
    public void update(Long id, HolidayRequest holidayRequest) {
        Holiday holiday = modelMapper.map(holidayRequest, Holiday.class);
        holiday.setDate(DateTimeUtil.stringToLocalDate(holidayRequest.getDate()));
        holiday.setId(id);

        holidayRepository.save(holiday);
    }

    @Override
    public void delete(Long id) {
        holidayRepository.delete(getHolidayById(id));
    }
}
