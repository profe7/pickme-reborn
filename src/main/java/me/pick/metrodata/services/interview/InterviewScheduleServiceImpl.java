package me.pick.metrodata.services.interview;

import lombok.RequiredArgsConstructor;
import me.pick.metrodata.enums.InterviewStatus;
import me.pick.metrodata.enums.InterviewType;
import me.pick.metrodata.exceptions.client.ClientDoesNotExistException;
import me.pick.metrodata.exceptions.interviewschedule.ApplicantNotRecommendedException;
import me.pick.metrodata.exceptions.interviewschedule.InterviewScheduleConflictException;
import me.pick.metrodata.exceptions.interviewschedule.InterviewScheduleDoesNotExistException;
import me.pick.metrodata.models.dto.requests.InterviewScheduleRequest;
import me.pick.metrodata.models.dto.requests.InterviewUpdateRequest;
import me.pick.metrodata.models.entity.*;
import me.pick.metrodata.repositories.ClientRepository;
import me.pick.metrodata.repositories.InterviewScheduleHistoryRepository;
import me.pick.metrodata.repositories.InterviewScheduleRepository;
import me.pick.metrodata.repositories.RecommendationApplicantRepository;
import me.pick.metrodata.repositories.specifications.InterviewScheduleSpecification;
import me.pick.metrodata.services.email.EmailService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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
    private final EmailService emailService;

    @Override
    public void inviteToInterview(InterviewScheduleRequest request) {
        RecommendationApplicant recommendedApplicant = recommendationApplicantRepository.findByApplicantIdAndPosition(request.getApplicantId(), request.getPosition()).orElseThrow(() -> new ApplicantNotRecommendedException(request.getApplicantId(), request.getPosition()));
        if (interviewConflictHelper(recommendedApplicant, request)) {
            throw new InterviewScheduleConflictException(request.getStartTime().toString(), request.getEndTime().toString(), request.getDate().toString());
        }

        Client client = clientRepository.findClientById(request.getClientId()).orElseThrow(() -> new ClientDoesNotExistException(request.getClientId()));
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
        Long interviewId = request.getInterviewId();
        InterviewStatus status = request.getStatus();
        String feedback = request.getFeedback();

        InterviewSchedule interviewSchedule = interviewScheduleRepository.findInterviewScheduleById(interviewId).orElseThrow(() -> new InterviewScheduleDoesNotExistException(interviewId));
        if (status == InterviewStatus.RESCHEDULED) {
            interviewSaveHelper(interviewSchedule, status, feedback);
            emailService.sendInterviewReschedule(interviewSchedule);
        } else if (status == InterviewStatus.CANCELLED) {
            interviewSaveHelper(interviewSchedule, status, feedback);
            emailService.sendInterviewCancel(interviewSchedule, feedback);
        } else if (status == InterviewStatus.ACCEPTED) {
            interviewSaveHelper(interviewSchedule, status, feedback);
            emailService.sendInterviewAccept(interviewSchedule, feedback);
        } else  if (status == InterviewStatus.REJECTED) {
            interviewSaveHelper(interviewSchedule, status, feedback);
            emailService.sendInterviewReject(interviewSchedule, feedback);
        }
    }

    private void interviewSaveHelper(InterviewSchedule interviewSchedule, InterviewStatus status, String feedback) {
        interviewSchedule.setStatus(status);
        interviewScheduleRepository.save(interviewSchedule);
        interviewHistorySaveHelper(interviewSchedule, feedback);
    }

    private void interviewHistorySaveHelper(InterviewSchedule interviewSchedule, String feedback) {
        InterviewScheduleHistory history = new InterviewScheduleHistory();
        history.setStatus(interviewSchedule.getStatus());
        history.setFeedback(feedback);
        history.setInterviewSchedule(interviewSchedule);
        interviewScheduleHistoryRepository.save(history);
    }

    private InterviewSchedule getInterviewSchedule(InterviewScheduleRequest request, RecommendationApplicant recommendedApplicant, Client client) {
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

    private boolean interviewConflictHelper(RecommendationApplicant recommendedApplicant, InterviewScheduleRequest request) {
        Client client = clientRepository.findClientById(request.getClientId()).orElseThrow(() -> new ClientDoesNotExistException(request.getClientId()));
        Applicant applicant = recommendedApplicant.getApplicant();

        InterviewSchedule found = interviewScheduleRepository.findInterviewScheduleByClientAndApplicantAndDate(client, applicant, request.getDate());
        List<InterviewSchedule> todaysInterviews = interviewScheduleRepository.findInterviewScheduleByClientAndDate(client, request.getDate());

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
            } else if (request.getStartTime().isAfter(request.getEndTime()) ||
                    request.getEndTime().equals(request.getStartTime()) ||
                    request.getStartTime().equals(request.getEndTime()) ||
                    request.getEndTime().isBefore(request.getStartTime())) {
                return true;
            }
        }

        return found != null;
    }

    public Page<InterviewSchedule> getAll(String search, Long clientId, InterviewType type, String startDate, String endDate, InterviewStatus status, int page, int size) {
        return interviewRetrievalHelper(search, null, type, startDate, endDate, status, page, size);
    }

    public Page<InterviewSchedule> getByRm(String search, Long clientId, InterviewType type, String startDate, String endDate, InterviewStatus status, int page, int size) {
        if (clientId == null) {
            throw new ClientDoesNotExistException(clientId);
        }

        return interviewRetrievalHelper(search, clientId, type, startDate, endDate, status, page, size);
    }

    public List<InterviewScheduleHistory> getTalentInterviewHistory(Long interviewId) {
        InterviewSchedule interviewSchedule = interviewScheduleRepository.findInterviewScheduleById(interviewId).orElseThrow(() -> new InterviewScheduleDoesNotExistException(interviewId));
        return interviewScheduleHistoryRepository.findInterviewScheduleHistoriesByInterviewSchedule(interviewSchedule);
    }

    private Page<InterviewSchedule> interviewRetrievalHelper(String search, Long clientId, InterviewType type, String startDate, String endDate, InterviewStatus status, int page, int size) {
        Specification<InterviewSchedule> spec = InterviewScheduleSpecification.searchSpecification(search, clientId, type, startDate, endDate, status);
        List<InterviewSchedule> schedules = interviewScheduleRepository.findAll(spec);
        Pageable pageable = PageRequest.of(page, size);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), schedules.size());

        return new PageImpl<>(schedules.subList(start, end), pageable, schedules.size());
    }
}
