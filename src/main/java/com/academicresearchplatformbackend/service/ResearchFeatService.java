package com.academicresearchplatformbackend.service;

import com.academicresearchplatformbackend.dao.FileResource;
import com.academicresearchplatformbackend.dao.ResearchFeat;
import org.springframework.data.domain.Page;

import java.util.Optional;


public interface ResearchFeatService {
    ResearchFeat addOne(ResearchFeat researchFeat);

    boolean deleteOne(Long id);

    boolean update(ResearchFeat newFeat);

    boolean addOneResource(Long id, FileResource resource);

    boolean addOneResource(Long fid, Long rid);

    Optional<ResearchFeat> findById(Long id);

    Page<ResearchFeat> findAllPageable(int page, int size);

    Page<ResearchFeat> findByType(String type, int page, int size);

    Page<ResearchFeat> findByLevel(int level, int page, int size);

    Page<ResearchFeat> findByIsPatent(boolean isPatent, int page, int size);
}
