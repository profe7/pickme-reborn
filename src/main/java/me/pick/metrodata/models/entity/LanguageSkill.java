package me.pick.metrodata.models.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.pick.metrodata.enums.AbilityLevel;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class LanguageSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer created_by;

    private Integer updated_by;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created_at;

    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updated_at;

    @Enumerated(EnumType.STRING)
    private AbilityLevel readingAbilityLevel;

    @Enumerated(EnumType.STRING)
    private AbilityLevel writingAbilityLevel;

    @Enumerated(EnumType.STRING)
    private AbilityLevel speakingAbilityLevel;

    @ManyToOne
    @JoinColumn(name = "language_id")
    private References language;

    @ManyToOne
    @JoinColumn(name = "talent_id")
    private Talent talent;
}
