package com.example.crm_dal.repositories;

import com.example.crm_dal.models.ArtistsAndGroups;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistsAndGroupsRepository extends JpaRepository<ArtistsAndGroups, Long> {
}