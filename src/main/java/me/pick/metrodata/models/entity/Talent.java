package me.pick.metrodata.models.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import me.pick.metrodata.enums.Gender;
import me.pick.metrodata.enums.MaritalStatus;
import me.pick.metrodata.enums.ReligionsEnum;
import me.pick.metrodata.enums.StatusCV;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {@Index(name = "idx_talent_name", columnList = "name"), @Index(name = "idx_talent_nik", columnList = "nik")})
public class Talent {
    @PrePersist
    public void generateId() {
        this.id = UUID.randomUUID().toString();
    }

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(length = 16)
    private String nik;

    private String placeOfBirth;

    @DateTimeFormat(pattern = "dd-MM-YYYY")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthOfDate;

    @Column(length = 64)
    private String phone;

    private String fullAddress;

    private String userUrl;

    @Lob
    @Comment("For Photo CV")
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] photo;

    @Lob
    @Comment("KTP, NPWP, Ijazah in one file")
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] file;

    private String created_by;

    private String updated_by;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private StatusCV statusCV;

    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;

    @Enumerated(EnumType.STRING)
    private ReligionsEnum religion;

    private float start_salary;

    private float end_salary;

    private Boolean Disabled;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created_at;

    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updated_at;

    @OneToMany(mappedBy = "talent")
    @JsonManagedReference
    private List<Applicant> applicants;

    @ManyToOne
    @JoinColumn(name = "institute_id", nullable = true)
    private Institute institute;

    @OneToMany(mappedBy = "talent", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OtherExperience> otherExperiences;

    @OneToMany(mappedBy = "talent", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Organization> organizations;

    @OneToMany(mappedBy = "talent", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Certification> certifications;

    @OneToMany(mappedBy = "talent", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Training> trainings;

    @OneToMany(mappedBy = "talent", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Project> projects;

    @OneToMany(mappedBy = "talent", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<JobHistory> jobHistories;

    @OneToMany(mappedBy = "talent", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<LanguageSkill> languageSkills;

    @OneToMany(mappedBy = "talent", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Education> educations;

    @OneToMany(mappedBy = "talent", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Achievements> achievement;

    @OneToMany(mappedBy = "talent", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Skill> skills;

    @ManyToOne
    @JoinColumn(name = "nationality_id", nullable = true)
    private References nationality;

    @ManyToOne
    @JoinColumn(name = "province_id", nullable = true)
    private References province;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = true)
    private References city;

    @ManyToOne
    @JoinColumn(name = "mitra_id", nullable = true)
    private Mitra mitra;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


    @Override
    public String toString() {
        return "Talent{" + "id='" + id + '\'' + '}';
    }

}