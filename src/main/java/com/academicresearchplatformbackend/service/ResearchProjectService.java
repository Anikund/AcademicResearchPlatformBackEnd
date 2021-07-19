package com.academicresearchplatformbackend.service;

import com.academicresearchplatformbackend.dao.FileResource;
import com.academicresearchplatformbackend.dao.ResearchProject;
import org.springframework.data.domain.Page;



public interface ResearchProjectService {
    ResearchProject addOne(ResearchProject project);

    boolean addResource(Long id, FileResource resource);

    boolean addResource(Long id, Long rid);

    boolean addUser(Long pid, Long uid);

    boolean addFeat(Long pid, Long fid);

    boolean update(ResearchProject project);

    boolean censor(Long id);

    boolean terminate(Long id);

    int daysToMidwayTime(Long id);

    int daysToTerminatingTime(Long id);

    Page<ResearchProject> findAllPageable(int page, int size);

}
