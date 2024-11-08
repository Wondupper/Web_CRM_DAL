package com.example.crm_dal.repositories;

import com.example.crm_dal.models.Track;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<Track, Long> {
}