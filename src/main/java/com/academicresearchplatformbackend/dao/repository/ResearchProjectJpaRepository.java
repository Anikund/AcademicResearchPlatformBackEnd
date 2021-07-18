package com.academicresearchplatformbackend.dao.repository;

import com.academicresearchplatformbackend.dao.ResearchProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResearchProjectJpaRepository extends JpaRepository<ResearchProject, Long> {
}
