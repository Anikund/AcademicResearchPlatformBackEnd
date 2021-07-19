package com.academicresearchplatformbackend.dao.repository;

import com.academicresearchplatformbackend.dao.ResearchFeat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResearchFeatJpaRepository extends JpaRepository<ResearchFeat, Long> {
    Page<ResearchFeat> findByType(String type, Pageable pageable);

    Page<ResearchFeat> findByLevel(int level, Pageable pageable);

    Page<ResearchFeat> findByIsPatent(boolean isPatent, Pageable pageable);

}
