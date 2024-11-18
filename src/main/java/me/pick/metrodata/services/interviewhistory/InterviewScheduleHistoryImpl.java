package me.pick.metrodata.services.interviewhistory;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.pick.metrodata.models.dto.responses.InterviewScheduleHistoryResponse;
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
}
