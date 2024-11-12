package me.pick.metrodata.models.entity;

import java.time.temporal.ChronoUnit;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.pick.metrodata.enums.ApplicantStatus;
import me.pick.metrodata.enums.VacancyStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Vacancy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String position;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    private Integer requiredPositions;

    @Enumerated(EnumType.STRING)
    private VacancyStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDate expiredDate;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "vacancy")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Applicant> applicants;

    @OneToMany(mappedBy = "vacancy")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Recommendation> recommendations;

    // Metode untuk menghitung hari sejak tanggal pembuatan
    public long getDaysSinceCreated() {
        return createdAt != null ? ChronoUnit.DAYS.between(createdAt.toLocalDate(), LocalDate.now()) : 0;

    @JsonProperty("totalApplicants")
    public Integer getTotalApplicants() {
        return (applicants != null) ? applicants.size() : 0;
    }

    @JsonProperty("totalTalentsFromRecommendations")
    public Integer getTotalTalentsFromRecommendations() {
        return (recommendations != null) ? recommendations.size() : 0;
    }

    @JsonProperty("totalApplicantsAccepted")
    public Integer getTotalApplicantsAccepted() {
        return (applicants != null)
                ? applicants.stream().filter(status -> status.getStatus().equals(ApplicantStatus.ACCEPTED))
                        .collect(Collectors.toList()).size()
                : 0;
    }
}
