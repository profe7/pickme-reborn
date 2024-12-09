package me.pick.metrodata.services.interview;

import java.io.ByteArrayOutputStream;

import lombok.RequiredArgsConstructor;
import me.pick.metrodata.enums.ApplicantStatus;
import me.pick.metrodata.enums.InterviewStatus;
import me.pick.metrodata.enums.InterviewType;
import me.pick.metrodata.exceptions.client.ClientDoesNotExistException;
import me.pick.metrodata.exceptions.interviewschedule.ApplicantNotRecommendedException;
import me.pick.metrodata.exceptions.interviewschedule.InterviewScheduleConflictException;
import me.pick.metrodata.exceptions.interviewschedule.InterviewScheduleDoesNotExistException;
import me.pick.metrodata.exceptions.interviewschedule.InterviewScheduleUpdateBadRequestException;
import me.pick.metrodata.exceptions.mitra.MitraDoesNotExistException;
import me.pick.metrodata.models.dto.requests.InterviewScheduleRequest;
import me.pick.metrodata.models.dto.requests.InterviewUpdateRequest;
import me.pick.metrodata.models.dto.responses.InterviewScheduleResponse;
import me.pick.metrodata.models.dto.responses.InterviewHistoryResponse;
import me.pick.metrodata.models.dto.responses.InterviewScheduleCalendarResponse;
import me.pick.metrodata.models.entity.*;
import me.pick.metrodata.repositories.*;
import me.pick.metrodata.repositories.specifications.InterviewScheduleSpecification;
import me.pick.metrodata.services.email.EmailService;
import me.pick.metrodata.utils.DateTimeUtil;

import org.modelmapper.ModelMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Service
@RequiredArgsConstructor
public class InterviewScheduleServiceImpl implements InterviewScheduleService {

    private final InterviewScheduleRepository interviewScheduleRepository;
    private final RecommendationApplicantRepository recommendationApplicantRepository;
    private final ClientRepository clientRepository;
    private final InterviewScheduleHistoryRepository interviewScheduleHistoryRepository;
    private final MitraRepository mitraRepository;
    private final EmailService emailService;
    private final ModelMapper modelMapper;

    @Override
    public void inviteToInterview(InterviewScheduleRequest request) {
        RecommendationApplicant recommendedApplicant = recommendationApplicantRepository
                .findByApplicantIdAndPosition(request.getApplicantId(), request.getPosition()).orElseThrow(
                        () -> new ApplicantNotRecommendedException(request.getApplicantId(), request.getPosition()));
        if (interviewConflictHelper(recommendedApplicant, request)) {
            throw new InterviewScheduleConflictException(request.getStartTime().toString(),
                    request.getEndTime().toString(), request.getDate().toString());
        }

        Client client = clientRepository.findClientById(request.getClientId())
                .orElseThrow(() -> new ClientDoesNotExistException(request.getClientId()));
        InterviewSchedule interviewSchedule = getInterviewSchedule(request, recommendedApplicant, client);
        interviewScheduleRepository.save(interviewSchedule);

        InterviewScheduleHistory history = new InterviewScheduleHistory();
        history.setFeedback("Interview invitation sent");
        history.setInterviewSchedule(interviewSchedule);
        interviewScheduleHistoryRepository.save(history);

        emailService.sendInterviewInvitation(interviewSchedule);
    }

    @Override
    public void updateInterviewStatus(InterviewUpdateRequest request) {
        InterviewSchedule interviewSchedule = interviewScheduleRepository
                .findInterviewScheduleById(request.getInterviewId())
                .orElseThrow(() -> new InterviewScheduleDoesNotExistException(request.getInterviewId()));

        if (request.getStatus() == InterviewStatus.RESCHEDULED) {
            if (request.getDate() == null || request.getStartTime() == null || request.getEndTime() == null) {
                throw new InterviewScheduleUpdateBadRequestException("Date, start time, and end time must be provided");
            }
            InterviewScheduleRequest check = new InterviewScheduleRequest();
            check.setDate(request.getDate().toString());
            check.setStartTime(request.getStartTime().toString());
            check.setEndTime(request.getEndTime().toString());
            check.setClientId(interviewSchedule.getClient().getId());
            RecommendationApplicant recommendedApplicant = recommendationApplicantRepository
                    .findByApplicantIdAndPosition(interviewSchedule.getApplicant().getId(),
                            interviewSchedule.getPosition())
                    .orElseThrow(() -> new ApplicantNotRecommendedException(interviewSchedule.getApplicant().getId(),
                            interviewSchedule.getPosition()));
            if (!interviewConflictHelper(recommendedApplicant, check)) {
                interviewSaveHelper(interviewSchedule, request);
                emailService.sendInterviewReschedule(interviewSchedule);
            } else {
                throw new InterviewScheduleConflictException(request.getStartTime().toString(),
                        request.getEndTime().toString(), request.getDate().toString());
            }
        } else if (request.getStatus() == InterviewStatus.CANCELLED) {
            interviewSaveHelper(interviewSchedule, request);
            emailService.sendInterviewCancel(interviewSchedule, request.getFeedback());
        } else if (request.getStatus() == InterviewStatus.ACCEPTED) {
            interviewSaveHelper(interviewSchedule, request);
            emailService.sendInterviewAccept(interviewSchedule, request.getFeedback());
        } else if (request.getStatus() == InterviewStatus.REJECTED) {
            interviewSaveHelper(interviewSchedule, request);
            emailService.sendInterviewReject(interviewSchedule, request.getFeedback());
        } else if (request.getStatus() == InterviewStatus.ON_PROCESS) {
            interviewSaveHelper(interviewSchedule, request);
        }
    }

    @Override
    public List<InterviewScheduleCalendarResponse> getInterviewCalendarClient(Long clientId) {
        Client client = clientRepository.findClientById(clientId)
                .orElseThrow(() -> new ClientDoesNotExistException(clientId));
        List<InterviewSchedule> schedules = interviewScheduleRepository.findInterviewScheduleByClient(client);
        return interviewCalendarHelper(schedules);
    }

    @Override
    public List<InterviewScheduleCalendarResponse> getInterviewCalendarMitra(Long mitraId) {
        Mitra mitra = mitraRepository.findMitraById(mitraId).orElseThrow(() -> new MitraDoesNotExistException(mitraId));
        List<InterviewSchedule> schedules = interviewScheduleRepository.findInterviewScheduleByMitraId(mitra.getId());
        return interviewCalendarHelper(schedules);
    }

    private List<InterviewScheduleCalendarResponse> interviewCalendarHelper(List<InterviewSchedule> schedules) {
        List<InterviewScheduleCalendarResponse> responses = new ArrayList<>();
        for (InterviewSchedule schedule : schedules) {
            InterviewScheduleCalendarResponse response = new InterviewScheduleCalendarResponse();
            response.setId(schedule.getId());
            response.setTitle(schedule.getApplicant().getTalent().getName());
            response.setStart(schedule.getDate());
            response.setEnd(schedule.getDate());
            response.setBackgroundColor(schedule.getStatus().toString());
            response.setAllDay(false);
            response.setEditable(false);
            responses.add(response);
        }
        return responses;
    }

    private void interviewSaveHelper(InterviewSchedule interviewSchedule, InterviewUpdateRequest request) {
        interviewSchedule.setStatus(request.getStatus());
        if (request.getDate() != null && request.getStartTime() != null && request.getEndTime() != null) {
            interviewSchedule.setDate(request.getDate());
            interviewSchedule.setStartTime(request.getStartTime());
            interviewSchedule.setEndTime(request.getEndTime());
        }
        if (request.getInterviewType() != null) {
            interviewSchedule.setInterviewType(request.getInterviewType());
        }
        if (request.getInterviewLink() != null) {
            interviewSchedule.setInterviewLink(request.getInterviewLink());
        }
        if (request.getStatus() == InterviewStatus.ACCEPTED) {
            Talent talent = interviewSchedule.getApplicant().getTalent();
            for (Applicant applicant : talent.getApplicants()) {
                applicant.setStatus(ApplicantStatus.REJECTED);
            }
            interviewSchedule.getApplicant().setStatus(ApplicantStatus.ACCEPTED);
            interviewSchedule.setOnBoardDate(request.getOnboarDate());
            for (InterviewSchedule interview : interviewSchedule.getApplicant().getInterviewSchedules()) {
                if (!Objects.equals(interview.getId(), interviewSchedule.getId())) {
                    interview.setStatus(InterviewStatus.REJECTED);
                }
            }
        }
        if (request.getStatus() == InterviewStatus.REJECTED) {
            interviewSchedule.getApplicant().setStatus(ApplicantStatus.REJECTED);
        }
        interviewScheduleRepository.save(interviewSchedule);
        interviewHistorySaveHelper(interviewSchedule, request.getFeedback());
    }

    private void interviewHistorySaveHelper(InterviewSchedule interviewSchedule, String feedback) {
        InterviewScheduleHistory history = new InterviewScheduleHistory();
        history.setStatus(interviewSchedule.getStatus());
        if (feedback == null) {
            feedback = "No feedback provided";
        }
        history.setFeedback(feedback);
        history.setInterviewSchedule(interviewSchedule);
        interviewScheduleHistoryRepository.save(history);
    }

    private InterviewSchedule getInterviewSchedule(InterviewScheduleRequest request,
            RecommendationApplicant recommendedApplicant, Client client) {
        Applicant applicant = recommendedApplicant.getApplicant();

        InterviewSchedule interviewSchedule = new InterviewSchedule();
        interviewSchedule.setPosition(request.getPosition());
        interviewSchedule.setDate(DateTimeUtil.stringToLocalDate(request.getDate()));
        interviewSchedule.setStartTime(LocalTime.parse(request.getStartTime()));
        interviewSchedule.setEndTime(LocalTime.parse(request.getEndTime()));
        interviewSchedule.setLocationAddress(request.getLocationAddress());
        interviewSchedule.setInterviewLink(request.getInterviewLink());
        interviewSchedule.setInterviewType(InterviewType.valueOf(request.getInterviewType()));
        interviewSchedule.setMessage(request.getMessage());
        interviewSchedule.setStatus(InterviewStatus.valueOf(request.getStatus()));
        interviewSchedule.setClient(client);
        interviewSchedule.setApplicant(applicant);
        return interviewSchedule;
    }

    private boolean interviewConflictHelper(RecommendationApplicant recommendedApplicant,
            InterviewScheduleRequest request) {
        Client client = clientRepository.findClientById(request.getClientId())
                .orElseThrow(() -> new ClientDoesNotExistException(request.getClientId()));
        Applicant applicant = recommendedApplicant.getApplicant();

        InterviewSchedule found = interviewScheduleRepository.findInterviewScheduleByClientAndApplicantAndDate(client,
                applicant, DateTimeUtil.stringToLocalDate(request.getDate()));
        List<InterviewSchedule> todaysInterviews = interviewScheduleRepository
                .findInterviewScheduleByClientAndDate(client, DateTimeUtil.stringToLocalDate(request.getDate()));

        List<LocalTime> reservedTimes = new ArrayList<>();

        for (InterviewSchedule interview : todaysInterviews) {
            reservedTimes.add(interview.getStartTime());
            reservedTimes.add(interview.getEndTime());
            LocalTime start = LocalTime.parse(request.getStartTime());
            LocalTime end = LocalTime.parse(request.getEndTime());
            while (start.isBefore(end)) {
                reservedTimes.add(start);
                start = start.plusMinutes(15);
            }
        }

        for (LocalTime time : reservedTimes) {
            if (LocalTime.parse(request.getStartTime()).isBefore(time)
                    && LocalTime.parse(request.getEndTime()).isAfter(time)) {
                return true;
            } else if (LocalTime.parse(request.getStartTime()).isAfter(LocalTime.parse(request.getEndTime()))
                    || LocalTime.parse(request.getEndTime()).equals(LocalTime.parse(request.getStartTime()))
                    || LocalTime.parse(request.getStartTime()).equals(LocalTime.parse(request.getEndTime()))
                    || LocalTime.parse(request.getEndTime()).isBefore(LocalTime.parse(request.getStartTime()))) {
                return true;
            }
        }

        return found != null;
    }

    @Override
    public Page<InterviewSchedule> getAll(String search, Long clientId, InterviewType type, String startDate,
            String endDate, InterviewStatus status, Long mitraId, int page, int size) {
        return interviewRetrievalHelper(search, clientId, type, startDate, endDate, status, mitraId, page, size);
    }

    @Override
    public Page<InterviewScheduleResponse> getFilteredInterviews(String searchRecruiter, String searchTalent,
            InterviewType type, LocalDate date, InterviewStatus status,
            int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return interviewScheduleRepository.findAllWithFilters(searchRecruiter, searchTalent, type, date, status,
                pageable).map(interviewSchedule -> {
                    InterviewScheduleResponse interviewScheduleResponse = modelMapper.map(interviewSchedule,
                            InterviewScheduleResponse.class);
                    interviewScheduleResponse
                            .setClientUserFirstName(interviewSchedule.getClient().getUser().getFirstName());
                    interviewScheduleResponse
                            .setApplicantTalentName(interviewSchedule.getApplicant().getTalent().getName());
                    return interviewScheduleResponse;
                });
    }

    @Override
    public Page<InterviewSchedule> getByRm(String search, Long clientId, InterviewType type, String startDate,
            String endDate, InterviewStatus status, Long mitraId, int page, int size) {
        if (clientId == null) {
            throw new ClientDoesNotExistException(clientId);
        }

        return interviewRetrievalHelper(search, clientId, type, startDate, endDate, status, mitraId, page, size);
    }

    @Override
    public List<InterviewHistoryResponse> getTalentInterviewHistory(Long interviewId) {
        InterviewSchedule interviewSchedule = interviewScheduleRepository.findInterviewScheduleById(interviewId)
                .orElseThrow(() -> new InterviewScheduleDoesNotExistException(interviewId));

        List<InterviewScheduleHistory> histories = interviewScheduleHistoryRepository
                .findInterviewScheduleHistoriesByInterviewSchedule(interviewSchedule);

        return histories.stream()
                .map(history -> new InterviewHistoryResponse(
                        interviewSchedule.getApplicant().getTalent().getName(),
                        history.getStatus().toString(),
                        history.getCreated_at()))
                .collect(Collectors.toList());
    }

    private Page<InterviewSchedule> interviewRetrievalHelper(String search, Long clientId, InterviewType type,
            String startDate, String endDate, InterviewStatus status, Long mitraId, int page, int size) {
        Specification<InterviewSchedule> spec = InterviewScheduleSpecification.searchSpecification(search, clientId,
                type, startDate, endDate, status, mitraId);
        List<InterviewSchedule> schedules = interviewScheduleRepository.findAll(spec);
        Pageable pageable = PageRequest.of(page, size);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), schedules.size());

        return new PageImpl<>(schedules.subList(start, end), pageable, schedules.size());
    }

    @Override
    public InterviewSchedule getById(Long id) {
        return interviewScheduleRepository.findById(id).orElse(null);
    }

    @Override
    public List<InterviewScheduleResponse> getByTalentId(String id) {
        return interviewScheduleRepository.findInterviewScheduleByTalentId(id).stream().map(interview -> {
            InterviewScheduleResponse interviewScheduleResponse = modelMapper.map(interview,
                    InterviewScheduleResponse.class);
            interviewScheduleResponse.setClientUserFirstName(interview.getClient().getUser().getFirstName());
            interviewScheduleResponse.setClientUserLastName(interview.getClient().getUser().getLastName());
            return interviewScheduleResponse;
        })
                .collect(Collectors.toList());
    }

    @Override
    public ByteArrayResource export(String searchRecruiter, String searchTalent, InterviewType type, LocalDate date,
            InterviewStatus status) {
        List<InterviewSchedule> interviews = interviewScheduleRepository.findAllWithFilters(searchRecruiter,
                searchTalent, type, date, status);

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Interview Schedules");

            // Define cell styles
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex()); // Soft blue color
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER); // Text alignment to center
            Font headerFont = workbook.createFont();
            headerFont.setFontName("Times New Roman");
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setAlignment(HorizontalAlignment.CENTER);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);
            Font dataFont = workbook.createFont();
            dataFont.setFontName("Times New Roman");
            dataFont.setFontHeightInPoints((short) 12);
            dataStyle.setFont(dataFont);

            Row headerRow = sheet.createRow(0);
            String[] headers = { "Perekrut", "Talent", "Posisi", "Wawancara", "Tanggal", "Waktu",
                    "Status" };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (InterviewSchedule interview : interviews) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(interview.getClient().getUser().getInstitute().getInstituteName());
                row.createCell(1).setCellValue(interview.getApplicant().getTalent().getName());
                row.createCell(2).setCellValue(interview.getPosition());
                row.createCell(3).setCellValue(interview.getInterviewType().toString());
                row.createCell(4).setCellValue(interview.getDate().toString());
                row.createCell(5).setCellValue(interview.getStartTime().toString() + " - " + interview.getEndTime()
                        .toString());
                row.createCell(6).setCellValue(interview.getStatus().toString());
                for (int i = 0; i < headers.length; i++) {
                    Cell cell = row.getCell(i);
                    if (cell == null) {
                        cell = row.createCell(i);
                    }
                    cell.setCellStyle(dataStyle);
                }
            }
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);

            return new ByteArrayResource(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
