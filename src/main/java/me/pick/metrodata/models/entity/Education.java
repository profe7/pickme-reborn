package me.pick.metrodata.models.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.pick.metrodata.enums.EducationalLevel;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Comment("jenjang pendidikan")
    private EducationalLevel educationalLevel;

    @DateTimeFormat(pattern = "dd-MM-YYYY")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "dd-MM-YYYY")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate endDate;

    @Column(nullable = false)
    @Comment("IPK/nilai")
    private Float academicGrade;

    private String institution;

    private Integer created_by;

    private Integer updated_by;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created_at;

    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updated_at;

    @ManyToOne
    @JoinColumn(name = "major_id")
    private References major;

    @ManyToOne
    @JoinColumn(name = "talent_id")
    @JsonBackReference
    private Talent talent;

    public Education(EducationalLevel educationalLevel, LocalDate startDate, LocalDate endDate, Float academicGrade, String institution, References references, Talent talent) {
        this.educationalLevel = educationalLevel;
        this.startDate = startDate;
        this.endDate = endDate;
        this.academicGrade = academicGrade;
        this.institution = institution;
        this.major = references;
        this.talent = talent;
    }
}
