package com.example.crm_dal.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.TimeZoneColumn;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "schedule")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "date", nullable = false, unique = true)
    private LocalDateTime time;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_track",nullable = false)
    private Track track;
}