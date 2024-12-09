package me.pick.metrodata.services.interviewhistory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.pick.metrodata.models.dto.responses.InterviewHistoryResponse;
import me.pick.metrodata.models.dto.responses.InterviewScheduleHistoryResponse;
import me.pick.metrodata.models.entity.InterviewScheduleHistory;
import me.pick.metrodata.repositories.InterviewScheduleHistoryRepository;

@Service
@RequiredArgsConstructor
public class InterviewScheduleHistoryImpl implements InterviewScheduleHistoryService {

        private final InterviewScheduleHistoryRepository interviewScheduleHistoryRepository;
        private final ModelMapper modelMapper;

        @Override
        public List<InterviewScheduleHistoryResponse> getByInterviewScheduleId(Long id) {
                return interviewScheduleHistoryRepository.findByInterviewSchedule_Id(id).stream()
                                .map(interview -> modelMapper.map(interview, InterviewScheduleHistoryResponse.class))
                                .collect(Collectors.toList());
        }

        @Override
        public List<InterviewHistoryResponse> getByTalentIdandClientId(String talentId, Long clientId){
                List<InterviewHistoryResponse> historyResponses = new ArrayList<>();
                List<InterviewScheduleHistory> historyRepo = interviewScheduleHistoryRepository.findInterviewScheduleHistoryByTalentIdAndClientId(talentId, clientId);

                for (InterviewScheduleHistory history : historyRepo){
                        InterviewHistoryResponse historyResponse = new InterviewHistoryResponse();
                        historyResponse.setNamaTalent(history.getInterviewSchedule().getApplicant().getTalent().getName());
                        historyResponse.setDateChanges(history.getCreated_at());
                        historyResponse.setStatus(history.getStatus().toString());

                        historyResponses.add(historyResponse);
                }
                return historyResponses;
        }


}
