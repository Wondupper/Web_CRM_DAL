package com.example.crm_dal.repositories;

import com.example.crm_dal.models.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
}