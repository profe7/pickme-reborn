package me.pick.metrodata.services.client;

import lombok.RequiredArgsConstructor;
import me.pick.metrodata.enums.ApplicantStatus;
import me.pick.metrodata.enums.InterviewStatus;
import me.pick.metrodata.exceptions.applicant.ApplicantDoesNotExistException;
import me.pick.metrodata.exceptions.client.ClientDoesNotExistException;
import me.pick.metrodata.exceptions.interviewschedule.InterviewScheduleDoesNotExistException;
import me.pick.metrodata.models.dto.responses.ClientDashboardTelemetryResponse;
import me.pick.metrodata.models.dto.responses.ClientResponse;
import me.pick.metrodata.models.dto.responses.PositionTelemetryResponse;
import me.pick.metrodata.models.entity.Applicant;
import me.pick.metrodata.models.entity.InterviewSchedule;
import me.pick.metrodata.models.entity.InterviewScheduleHistory;
import me.pick.metrodata.repositories.ApplicantRepository;
import me.pick.metrodata.repositories.ClientRepository;
import me.pick.metrodata.repositories.InterviewScheduleHistoryRepository;
import me.pick.metrodata.repositories.InterviewScheduleRepository;
import me.pick.metrodata.models.entity.Talent;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final InterviewScheduleRepository interviewScheduleRepository;
    private final ClientRepository clientRepository;
    private final ApplicantRepository applicantRepository;
    private final InterviewScheduleHistoryRepository interviewScheduleHistoryRepository;
    private final ModelMapper modelMapper;

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
        Applicant employee = applicantRepository.findByTalent_IdAndStatus(talentId, ApplicantStatus.ACCEPTED)
                .orElseThrow(() -> new ApplicantDoesNotExistException(404L));
        employee.setStatus(ApplicantStatus.INACTIVE);
        applicantRepository.save(employee);
        InterviewSchedule schedule = interviewScheduleRepository
                .findByApplicantAndClientIdAndStatus(employee, clientId, InterviewStatus.ACCEPTED)
                .orElseThrow(() -> new InterviewScheduleDoesNotExistException(clientId));
        schedule.setStatus(InterviewStatus.INACTIVE);
        interviewScheduleRepository.save(schedule);

        InterviewScheduleHistory history = new InterviewScheduleHistory();
        history.setInterviewSchedule(schedule);
        history.setStatus(InterviewStatus.INACTIVE);
        interviewScheduleHistoryRepository.save(history);
    }

    @Override
    public ClientDashboardTelemetryResponse getClientDashboardTelemetry(Long clientId) {
        clientRepository.findById(clientId).orElseThrow(() -> new ClientDoesNotExistException(clientId));
        ClientDashboardTelemetryResponse response = new ClientDashboardTelemetryResponse();
        response.setClientId(clientId);
        response.setTotalEmployees(((long) clientRepository.findEmployeeByInterviewAccepted(clientId).size()));
        response.setTotalEmployeesByPosition(positionTelemetryHelper(clientId));
        return response;
    }

    private List<PositionTelemetryResponse> positionTelemetryHelper(Long clientId) {
        List<Object[]> telemetry = clientRepository.findUniquePositionsAndCountByClientId(clientId);
        List<PositionTelemetryResponse> responses = new ArrayList<>();
        telemetry.forEach(objects -> {
            PositionTelemetryResponse response = new PositionTelemetryResponse();
            response.setPositionName((String) objects[0]);
            response.setTotalEmployees((Long) objects[1]);
            responses.add(response);
        });
        return responses;
    }

    @Override
    public List<ClientResponse> getClients() {
        return clientRepository.findAll().stream().map(client -> {
            ClientResponse clientResponse = modelMapper.map(client, ClientResponse.class);
            clientResponse.setClientName(client.getUser().getFirstName() + ' ' + client.getUser().getLastName());
            return clientResponse;
        })
                .collect(Collectors.toList());
    }
}
