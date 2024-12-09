package me.pick.metrodata.models.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.pick.metrodata.enums.ContractStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class JobHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;

    @Enumerated(EnumType.STRING)
    private ContractStatus contractStatus;

    @DateTimeFormat(pattern = "dd-MM-YYYY")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "dd-MM-YYYY")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate endDate;

    private String description;

    private String projectSpecification;

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
    @JoinColumn(name = "position_id")
    @JsonBackReference
    private References position;

    public JobHistory(String companyName, ContractStatus contractStatus, LocalDate startDate, LocalDate endDate,
            String description, String projectSpecification, Talent talent, References references) {
        this.companyName = companyName;
        this.contractStatus = contractStatus;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.projectSpecification = projectSpecification;
        this.talent = talent;
        this.position = references;
    }
}
