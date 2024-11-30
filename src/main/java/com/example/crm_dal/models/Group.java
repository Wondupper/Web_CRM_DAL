package com.example.crm_dal.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "groups")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Builder(toBuilder=true)
@AllArgsConstructor
@NoArgsConstructor
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER,cascade=CascadeType.REFRESH)
    @JoinColumn(name = "id_genre", nullable = false)
    @JsonBackReference
    private Genre genre;

    @OneToMany(mappedBy = "group" ,fetch = FetchType.LAZY,cascade=CascadeType.ALL)
    @JsonManagedReference
    private List<Artist> artists;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY,cascade=CascadeType.ALL)
    @JsonManagedReference
    private List<Track> tracks;
}