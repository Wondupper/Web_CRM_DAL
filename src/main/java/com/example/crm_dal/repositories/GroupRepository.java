package com.example.crm_dal.repositories;

import com.example.crm_dal.models.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
    public Group findByName(String name);
}