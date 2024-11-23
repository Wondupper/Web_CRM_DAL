package com.example.crm_dal.repositories;

import com.example.crm_dal.models.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    public Genre findByName(String name);
}