package me.pick.metrodata.services.interview;

import me.pick.metrodata.enums.InterviewStatus;
import me.pick.metrodata.exceptions.client.ClientDoesNotExistException;
import me.pick.metrodata.exceptions.interviewschedule.ApplicantNotRecommendedException;
import me.pick.metrodata.exceptions.interviewschedule.InterviewScheduleConflictException;
import me.pick.metrodata.models.dto.requests.InterviewScheduleRequest;
import me.pick.metrodata.models.entity.*;
import me.pick.metrodata.repositories.ClientRepository;
import me.pick.metrodata.repositories.InterviewScheduleHistoryRepository;
import me.pick.metrodata.repositories.InterviewScheduleRepository;
import me.pick.metrodata.repositories.RecommendationApplicantRepository;
import me.pick.metrodata.services.email.EmailService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class InterviewScheduleServiceImpl implements InterviewScheduleService {
    private final InterviewScheduleRepository interviewScheduleRepository;
    private final RecommendationApplicantRepository recommendationApplicantRepository;
    private final ClientRepository clientRepository;
    private final InterviewScheduleHistoryRepository interviewScheduleHistoryRepository;
    private final EmailService emailService;
    private final ModelMapper modelMapper = new ModelMapper();

    public InterviewScheduleServiceImpl(InterviewScheduleRepository interviewScheduleRepository, RecommendationApplicantRepository recommendationApplicantRepository,
                                        ClientRepository clientRepository, InterviewScheduleHistoryRepository interviewScheduleHistoryRepository,
                                        EmailService emailService) {
        this.interviewScheduleRepository = interviewScheduleRepository;
        this.recommendationApplicantRepository = recommendationApplicantRepository;
        this.clientRepository = clientRepository;
        this.interviewScheduleHistoryRepository = interviewScheduleHistoryRepository;
        this.emailService = emailService;
    }

    @Override
    public void inviteToInterview(InterviewScheduleRequest request) {
        RecommendationApplicant recommendedApplicant = recommendationApplicantRepository.findByApplicantIdAndPosition(request.getApplicantId(), request.getPosition()).orElseThrow(() -> new ApplicantNotRecommendedException(request.getApplicantId(), request.getPosition()));
        if (interviewConflictHelper(recommendedApplicant, request)) {
            throw new InterviewScheduleConflictException(request.getStartTime().toString(), request.getEndTime().toString(), request.getDate().toString());
        }

        Client client = clientRepository.findClientById(request.getClientId()).orElseThrow(() -> new ClientDoesNotExistException(request.getClientId()));
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
        interviewScheduleRepository.save(interviewSchedule);

        InterviewScheduleHistory history = new InterviewScheduleHistory();
        history.setFeedback(null);
        history.setInterviewSchedule(interviewSchedule);
        interviewScheduleHistoryRepository.save(history);

        emailService.sendInterviewInvitation(interviewSchedule);
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

        if (found != null) {
            return true;
        }
        return false;
    }
}
