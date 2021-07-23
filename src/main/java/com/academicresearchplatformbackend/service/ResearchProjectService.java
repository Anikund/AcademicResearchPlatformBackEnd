package com.academicresearchplatformbackend.service;

import com.academicresearchplatformbackend.dao.FileResource;
import com.academicresearchplatformbackend.dao.ResearchProject;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;


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

    List<ResearchProject> findAll();

    Optional<ResearchProject> findById(Long id);
    boolean assignFund(Long pid, int amout);
    boolean consumeFund(Long pid, int amount);

    boolean midExamine(Long pid);

    ResearchProject deleteById(Long id);
}
