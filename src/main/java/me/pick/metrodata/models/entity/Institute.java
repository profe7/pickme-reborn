package me.pick.metrodata.models.entity;

import java.util.List;


import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.pick.metrodata.enums.InstituteType;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Institute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String instituteName;

    private String companyName;

    private String email;

    @Enumerated(EnumType.STRING)
    private InstituteType instituteType;

    @ManyToOne
    @JoinColumn(name = "rm_id", nullable = true)
    private User rmUser;

    @OneToMany(mappedBy = "institute")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<User> users;

    @OneToMany(mappedBy = "institute")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Talent> talents;

}
