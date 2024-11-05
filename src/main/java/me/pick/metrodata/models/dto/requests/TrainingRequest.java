package me.pick.metrodata.models.dto.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TrainingRequest {

    @NotNull
    private String trainingName;

    @NotNull
    @DateTimeFormat(pattern = "dd-MM-YYYY")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate trainingDate;

    @NotNull
    private String syllabus;

    @NotNull
    private String talentId;

}
