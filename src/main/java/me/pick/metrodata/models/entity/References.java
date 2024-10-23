package me.pick.metrodata.models.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reference_param", indexes = {@Index(name = "idx_reference_group1", columnList = "reference_group1"), @Index(name = "idx_reference_group2", columnList = "reference_group2")})
public class References {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reference_name;

    private String reference_group1;

    private Integer reference_group2;

    private String remarks;

    private Boolean is_active;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created_at;

    private Integer created_by;

    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updated_at;

    private Integer updated_by;

    @OneToMany(mappedBy = "skill")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Project> projects;

    @OneToMany(mappedBy = "position")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<JobHistory> job_histories_as_position;

    @OneToMany(mappedBy = "major")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Education> majors;

    @OneToMany(mappedBy = "nationality")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Talent> nationalities;

    @OneToMany(mappedBy = "religion")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Talent> religions;

    @OneToMany(mappedBy = "province")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Talent> provinces;

    @OneToMany(mappedBy = "city")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Talent> cities;

    @OneToMany(mappedBy = "language")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<LanguageSkill> languageSkills;

}

