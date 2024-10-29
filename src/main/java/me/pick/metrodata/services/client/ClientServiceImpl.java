package me.pick.metrodata.services.client;

import me.pick.metrodata.enums.ApplicantStatus;
import me.pick.metrodata.enums.InterviewStatus;
import me.pick.metrodata.exceptions.applicant.ApplicantDoesNotExistException;
import me.pick.metrodata.exceptions.client.ClientDoesNotExistException;
import me.pick.metrodata.exceptions.interviewschedule.InterviewScheduleDoesNotExistException;
import me.pick.metrodata.models.entity.Applicant;
import me.pick.metrodata.models.entity.InterviewSchedule;
import me.pick.metrodata.models.entity.InterviewScheduleHistory;
import me.pick.metrodata.repositories.ApplicantRepository;
import me.pick.metrodata.repositories.ClientRepository;
import me.pick.metrodata.repositories.InterviewScheduleHistoryRepository;
import me.pick.metrodata.repositories.InterviewScheduleRepository;
import me.pick.metrodata.models.entity.Talent;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    private final InterviewScheduleRepository interviewScheduleRepository;
    private final ClientRepository clientRepository;
    private final ApplicantRepository applicantRepository;
    private final InterviewScheduleHistoryRepository interviewScheduleHistoryRepository;

    public ClientServiceImpl(InterviewScheduleRepository interviewScheduleRepository, ClientRepository clientRepository, ApplicantRepository applicantRepository, InterviewScheduleHistoryRepository interviewScheduleHistoryRepository) {
        this.interviewScheduleRepository = interviewScheduleRepository;
        this.clientRepository = clientRepository;
        this.applicantRepository = applicantRepository;
        this.interviewScheduleHistoryRepository = interviewScheduleHistoryRepository;
    }

    @Override
    public List<Talent> getClientEmployees(Long clientId) {
        clientRepository.findById(clientId).orElseThrow(() -> new ClientDoesNotExistException(clientId));
        return interviewScheduleRepository.findInterviewScheduleByClientIdAndStatus(clientId, InterviewStatus.ACCEPTED)
                .stream()
                .map(interviewSchedule -> interviewSchedule.getApplicant().getTalent())
                .toList();
    }

    @Override
    public void deleteClientEmployee(Long clientId, String talentId) {
        Applicant employee = applicantRepository.findByTalent_IdAndStatus(talentId, ApplicantStatus.ACCEPTED).orElseThrow(() -> new ApplicantDoesNotExistException(404L));
        employee.setStatus(ApplicantStatus.INACTIVE);
        applicantRepository.save(employee);
        InterviewSchedule schedule = interviewScheduleRepository.findByApplicantAndClientIdAndStatus(employee, clientId, InterviewStatus.ACCEPTED).orElseThrow(() -> new InterviewScheduleDoesNotExistException(clientId));
        schedule.setStatus(InterviewStatus.INACTIVE);
        interviewScheduleRepository.save(schedule);

        InterviewScheduleHistory history = new InterviewScheduleHistory();
        history.setInterviewSchedule(schedule);
        history.setStatus(InterviewStatus.INACTIVE);
        interviewScheduleHistoryRepository.save(history);
    }
}
