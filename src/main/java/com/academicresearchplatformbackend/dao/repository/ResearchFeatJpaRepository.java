package com.academicresearchplatformbackend.dao.repository;

import com.academicresearchplatformbackend.dao.ResearchFeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResearchFeatJpaRepository extends JpaRepository<ResearchFeat, Long> {
}
