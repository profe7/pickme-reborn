package me.pick.metrodata.models.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String projectName;

    @Column(nullable = false, length = 64)
    private String institution;

    private String description;

    @DateTimeFormat(pattern = "dd-MM-YYYY")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "dd-MM-YYYY")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate endDate;

    private Integer created_by;

    private Integer updated_by;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created_at;

    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updated_at;

    @ManyToOne
    @JoinColumn(name = "talent_id")
    @JsonBackReference
    private Talent talent;

    @ManyToOne
    @JoinColumn(name = "skill_id")
    private References skill;

    public Project(String projectName, String institution, String description, LocalDate startDate, LocalDate endDate, Talent talent, References references) {
        this.projectName = projectName;
        this.institution = institution;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.talent = talent;
        this.skill = references;
    }
}
