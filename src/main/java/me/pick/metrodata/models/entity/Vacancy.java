package me.pick.metrodata.models.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.pick.metrodata.enums.VacancyStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vacancy {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String position;

    @Column (name = "description", columnDefinition = "TEXT")
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
}

