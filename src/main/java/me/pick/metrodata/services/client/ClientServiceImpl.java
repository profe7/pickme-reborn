package me.pick.metrodata.services.client;

import me.pick.metrodata.enums.InterviewStatus;
import me.pick.metrodata.exceptions.client.ClientDoesNotExistException;
import me.pick.metrodata.repositories.ClientRepository;
import me.pick.metrodata.repositories.InterviewScheduleRepository;
import me.pick.metrodata.models.entity.Talent;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    private final InterviewScheduleRepository interviewScheduleRepository;
    private final ClientRepository clientRepository;

    public ClientServiceImpl(InterviewScheduleRepository interviewScheduleRepository, ClientRepository clientRepository) {
        this.interviewScheduleRepository = interviewScheduleRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public List<Talent> getClientEmployees(Long clientId) {
        clientRepository.findById(clientId).orElseThrow(() -> new ClientDoesNotExistException(clientId));
        return interviewScheduleRepository.findInterviewScheduleByClientIdAndStatus(clientId, InterviewStatus.ACCEPTED)
                .stream()
                .map(interviewSchedule -> interviewSchedule.getApplicant().getTalent())
                .toList();
    }
}
