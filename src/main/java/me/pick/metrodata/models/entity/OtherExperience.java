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
@Table
public class OtherExperience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64, nullable = false)
    private String experienceName;

    @Column(length = 100, nullable = false)
    private String instituteName;

    @Column(length = 64, nullable = false)
    private String positionName;

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "dd-MM-YYYY")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate experienceDate;

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

    public OtherExperience(String experienceName, String instituteName, String positionName, String positionName1, LocalDate experienceDate, Talent talent) {
        this.experienceName = experienceName;
        this.instituteName = instituteName;
        this.positionName = positionName;
        this.description = positionName1;
        this.experienceDate = experienceDate;
        this.talent = talent;
    }
}
