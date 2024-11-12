package me.pick.metrodata.services.interview;

import lombok.RequiredArgsConstructor;
import me.pick.metrodata.enums.InterviewStatus;
import me.pick.metrodata.enums.InterviewType;
import me.pick.metrodata.exceptions.client.ClientDoesNotExistException;
import me.pick.metrodata.exceptions.interviewschedule.ApplicantNotRecommendedException;
import me.pick.metrodata.exceptions.interviewschedule.InterviewScheduleConflictException;
import me.pick.metrodata.exceptions.interviewschedule.InterviewScheduleDoesNotExistException;
import me.pick.metrodata.exceptions.interviewschedule.InterviewScheduleUpdateBadRequestException;
import me.pick.metrodata.exceptions.mitra.MitraDoesNotExistException;
import me.pick.metrodata.exceptions.user.UserDoesNotExistException;
import me.pick.metrodata.models.dto.requests.InterviewScheduleRequest;
import me.pick.metrodata.models.dto.requests.InterviewUpdateRequest;
import me.pick.metrodata.models.dto.responses.InterviewScheduleResponse;
import me.pick.metrodata.models.dto.responses.InterviewScheduleCalendarResponse;
import me.pick.metrodata.models.entity.*;
import me.pick.metrodata.repositories.*;
import me.pick.metrodata.repositories.specifications.InterviewScheduleSpecification;
import me.pick.metrodata.services.email.EmailService;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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
            check.setDate(request.getDate());
            check.setStartTime(request.getStartTime());
            check.setEndTime(request.getEndTime());
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
        }
    }

    @Override
    public List<InterviewScheduleCalendarResponse> getInterviewCalendarClient(Long clientId) {
        Client client = clientRepository.findClientById(clientId).orElseThrow(() -> new ClientDoesNotExistException(clientId));
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
        interviewSchedule.setDate(request.getDate());
        interviewSchedule.setStartTime(request.getStartTime());
        interviewSchedule.setEndTime(request.getEndTime());
        interviewSchedule.setLocationAddress(request.getLocationAddress());
        interviewSchedule.setInterviewLink(request.getInterviewLink());
        interviewSchedule.setInterviewType(request.getInterviewType());
        interviewSchedule.setMessage(request.getMessage());
        interviewSchedule.setStatus(request.getStatus());
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
                applicant, request.getDate());
        List<InterviewSchedule> todaysInterviews = interviewScheduleRepository
                .findInterviewScheduleByClientAndDate(client, request.getDate());

        List<LocalTime> reservedTimes = new ArrayList<>();

        for (InterviewSchedule interview : todaysInterviews) {
            reservedTimes.add(interview.getStartTime());
            reservedTimes.add(interview.getEndTime());
            LocalTime start = request.getStartTime();
            LocalTime end = request.getEndTime();
            while (start.isBefore(end)) {
                reservedTimes.add(start);
                start = start.plusMinutes(15);
            }
        }

        for (LocalTime time : reservedTimes) {
            if (request.getStartTime().isBefore(time) && request.getEndTime().isAfter(time)) {
                return true;
            } else if (request.getStartTime().isAfter(request.getEndTime())
                    || request.getEndTime().equals(request.getStartTime())
                    || request.getStartTime().equals(request.getEndTime())
                    || request.getEndTime().isBefore(request.getStartTime())) {
                return true;
            }
        }

        return found != null;
    }

    @Override
    public Page<InterviewSchedule> getAll(String search, Long clientId, InterviewType type, String startDate,
            String endDate, InterviewStatus status, int page, int size) {
        return interviewRetrievalHelper(search, null, type, startDate, endDate, status, page, size);
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
            String endDate, InterviewStatus status, int page, int size) {
        if (clientId == null) {
            throw new ClientDoesNotExistException(clientId);
        }

        return interviewRetrievalHelper(search, clientId, type, startDate, endDate, status, page, size);
    }

    @Override
    public List<InterviewScheduleHistory> getTalentInterviewHistory(Long interviewId) {
        InterviewSchedule interviewSchedule = interviewScheduleRepository.findInterviewScheduleById(interviewId)
                .orElseThrow(() -> new InterviewScheduleDoesNotExistException(interviewId));
        return interviewScheduleHistoryRepository.findInterviewScheduleHistoriesByInterviewSchedule(interviewSchedule);
    }

    private Page<InterviewSchedule> interviewRetrievalHelper(String search, Long clientId, InterviewType type,
            String startDate, String endDate, InterviewStatus status, int page, int size) {
        Specification<InterviewSchedule> spec = InterviewScheduleSpecification.searchSpecification(search, clientId,
                type, startDate, endDate, status);
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
}
