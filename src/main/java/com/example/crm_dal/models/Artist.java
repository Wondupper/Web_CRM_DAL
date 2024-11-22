package com.example.crm_dal.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "artists")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_genre", nullable = false)
    private Genre genre;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "artists_groups",
            joinColumns = { @JoinColumn(name = "id_artist") },
            inverseJoinColumns = { @JoinColumn(name = "id_group") }
    )
    private List<Group> groups;
}