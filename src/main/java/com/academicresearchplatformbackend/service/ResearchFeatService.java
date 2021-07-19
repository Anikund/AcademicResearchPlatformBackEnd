package com.academicresearchplatformbackend.service;

import com.academicresearchplatformbackend.dao.FileResource;
import com.academicresearchplatformbackend.dao.ResearchFeat;
import org.springframework.data.domain.Page;

import java.util.Optional;


public interface ResearchFeatService {
    ResearchFeat addOne(ResearchFeat researchFeat);

    boolean deleteOne(Long id);

    boolean updateType(ResearchFeat newFeat);

    boolean addOneResource(Long id, FileResource resource);

    Optional<ResearchFeat> findById(Long id);

    Page<ResearchFeat> findAllPageable(int page, int size);


}
