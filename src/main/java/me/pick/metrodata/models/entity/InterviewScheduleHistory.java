package me.pick.metrodata.models.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.pick.metrodata.enums.InterviewStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class InterviewScheduleHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String feedback;

    @Enumerated(EnumType.STRING)
    private InterviewStatus status;

    @CreationTimestamp
    private LocalDate created_at;

    @UpdateTimestamp
    private LocalDate updated_at;

    @ManyToOne
    @JoinColumn(name = "interview_id")
    private InterviewSchedule interviewSchedule;
}
