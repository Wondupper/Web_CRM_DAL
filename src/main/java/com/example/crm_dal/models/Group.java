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
@Table(name = "groups")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @ManyToMany(mappedBy = "groups",fetch = FetchType.EAGER)
    private List<Artist> artists;

    @OneToMany(mappedBy = "group", fetch = FetchType.EAGER)
    private List<Track> tracks;
}