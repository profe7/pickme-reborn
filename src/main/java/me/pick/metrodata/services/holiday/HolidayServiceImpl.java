package me.pick.metrodata.services.holiday;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

import lombok.RequiredArgsConstructor;
import me.pick.metrodata.models.dto.requests.HolidayRequest;
import me.pick.metrodata.models.dto.responses.HolidayApiResponse;
import me.pick.metrodata.models.dto.responses.HolidayDataResponse;
import me.pick.metrodata.models.dto.responses.HolidayResponse;
import me.pick.metrodata.models.entity.Holiday;
import me.pick.metrodata.repositories.HolidayRepository;
import me.pick.metrodata.utils.AnyUtil;
import me.pick.metrodata.utils.DateTimeUtil;

@RequiredArgsConstructor
@Service
public class HolidayServiceImpl implements HolidayService {

    private final HolidayRepository holidayRepository;
    private final ModelMapper modelMapper;
    private final ResourceLoader resourceLoader;

    @Value("${upload.path}")
    private String uploadPath;

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

    @Override
    public HolidayApiResponse readJSONFile(String fileName) {
        // for local
        try {
            Resource resource = resourceLoader.getResource("classpath:static/json/" + fileName);
            String absolutePath = resource.getFile().getAbsolutePath();
            String jsonContent = new String(Files.readAllBytes(Paths.get(absolutePath)));
            ObjectMapper objectMapper = new ObjectMapper();
            HolidayApiResponse dataObject = objectMapper.readValue(jsonContent, HolidayApiResponse.class);
            return dataObject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        // for deploy
        // try {
        // // Menggunakan InputStream untuk membaca file dari classpath
        // String filePath = "/home/static/json/" + fileName;

        // InputStream inputStream = new FileInputStream(filePath);

        // if (inputStream == null) {
        // System.err.println("File not found: " + fileName);
        // return null;
        // }

        // // Membaca isi file menjadi string
        // Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
        // String jsonContent = scanner.hasNext() ? scanner.next() : "";

        // // Menggunakan ObjectMapper untuk mengonversi JSON menjadi objek
        // ObjectMapper objectMapper = new ObjectMapper();
        // HolidayApiResponse dataObject = objectMapper.readValue(jsonContent,
        // HolidayApiResponse.class);

        // return dataObject;
        // } catch (Exception e) {
        // e.printStackTrace();
        // return null;
        // }
    }

    private Holiday getByDate(LocalDate date) {
        return holidayRepository.findByDate(date).orElse(null);
    }

    @Override
    public Boolean uploadJSONFile(MultipartFile file) {
        try {
            File directory = new File(uploadPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String originalFileName = file.getOriginalFilename();
            String filePath = uploadPath + File.separator + originalFileName;

            FileCopyUtils.copy(file.getBytes(), new File(filePath));

            HolidayApiResponse holidayApiResponse = null;

            while (holidayApiResponse == null) {
                holidayApiResponse = readJSONFile(originalFileName);
            }

            Map<String, HolidayResponse> holidays = holidayApiResponse.getData().getHoliday();
            Map<String, HolidayResponse> leaves = holidayApiResponse.getData().getLeave();
            Map<String, HolidayResponse> islamics = holidayApiResponse.getData().getIslamic();
            Map<String, HolidayResponse> longWeekends = holidayApiResponse.getData().getLongWeekend();
            Map<String, HolidayResponse> harpitnas = holidayApiResponse.getData().getHarpitnas();

            Integer[] includedModes = { 0, 1 };

            for (Map.Entry<String, HolidayResponse> entry : holidays.entrySet()) {
                List<HolidayDataResponse> data = entry.getValue().getData();
                if (data != null) {
                    for (HolidayDataResponse d : data) {
                        LocalDate date = LocalDate.parse(d.getDate());
                        Holiday holiday = new Holiday();
                        holiday.setDate(date);
                        holiday.setName(d.getName());
                        holiday.setDescription("Holiday");

                        Holiday oldHoliday = getByDate(date);
                        if (AnyUtil.isNumberInArray(includedModes, d.getMode())) {
                            if (oldHoliday != null) {
                                holiday.setId(oldHoliday.getId());
                            }
                            holidayRepository.save(holiday);
                        }
                    }
                }
            }

            for (Map.Entry<String, HolidayResponse> entry : leaves.entrySet()) {
                List<HolidayDataResponse> data = entry.getValue().getData();
                if (data != null) {
                    for (HolidayDataResponse d : data) {
                        LocalDate date = LocalDate.parse(d.getDate());
                        Holiday holiday = new Holiday();
                        holiday.setDate(date);
                        holiday.setName(d.getName());
                        holiday.setDescription("Leave");

                        Holiday oldHoliday = getByDate(date);
                        if (AnyUtil.isNumberInArray(includedModes, d.getMode())) {
                            if (oldHoliday != null) {
                                holiday.setId(oldHoliday.getId());
                            }
                            holidayRepository.save(holiday);
                        }
                    }
                }
            }

            for (Map.Entry<String, HolidayResponse> entry : islamics.entrySet()) {
                List<HolidayDataResponse> data = entry.getValue().getData();
                if (data != null) {
                    for (HolidayDataResponse d : data) {
                        LocalDate date = LocalDate.parse(d.getDate());
                        Holiday holiday = new Holiday();
                        holiday.setDate(date);
                        holiday.setName(d.getName());
                        holiday.setDescription("Islamic");

                        Holiday oldHoliday = getByDate(date);
                        if (AnyUtil.isNumberInArray(includedModes, d.getMode())) {
                            if (oldHoliday != null) {
                                holiday.setId(oldHoliday.getId());
                            }
                            holidayRepository.save(holiday);
                        }
                    }
                }
            }

            for (Map.Entry<String, HolidayResponse> entry : longWeekends.entrySet()) {
                List<HolidayDataResponse> data = entry.getValue().getData();
                if (data != null) {
                    for (HolidayDataResponse d : data) {
                        LocalDate date = LocalDate.parse(d.getDate());
                        Holiday holiday = new Holiday();
                        holiday.setDate(date);
                        holiday.setName(d.getName());
                        holiday.setDescription("Long Weekend");

                        Holiday oldHoliday = getByDate(date);
                        if (AnyUtil.isNumberInArray(includedModes, d.getMode())) {
                            if (oldHoliday != null) {
                                holiday.setId(oldHoliday.getId());
                            }
                            holidayRepository.save(holiday);
                        }
                    }
                }
            }

            for (Map.Entry<String, HolidayResponse> entry : harpitnas.entrySet()) {
                List<HolidayDataResponse> data = entry.getValue().getData();
                if (data != null) {
                    for (HolidayDataResponse d : data) {
                        LocalDate date = LocalDate.parse(d.getDate());
                        Holiday holiday = new Holiday();
                        holiday.setDate(date);
                        holiday.setName(d.getName());
                        holiday.setDescription("Harpitnas");

                        Holiday oldHoliday = getByDate(date);
                        if (AnyUtil.isNumberInArray(includedModes, d.getMode())) {
                            if (oldHoliday != null) {
                                holiday.setId(oldHoliday.getId());
                            }
                            holidayRepository.save(holiday);
                        }
                    }
                }
            }

            return Boolean.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }
}
