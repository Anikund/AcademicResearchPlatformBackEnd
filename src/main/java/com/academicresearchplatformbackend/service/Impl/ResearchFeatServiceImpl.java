package com.academicresearchplatformbackend.service.Impl;

import com.academicresearchplatformbackend.dao.FileResource;
import com.academicresearchplatformbackend.dao.ResearchFeat;
import com.academicresearchplatformbackend.dao.repository.ResearchFeatJpaRepository;
import com.academicresearchplatformbackend.service.FileResourceService;
import com.academicresearchplatformbackend.service.ResearchFeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ResearchFeatServiceImpl implements ResearchFeatService {
    private ResearchFeatJpaRepository researchFeatJpaRepository;
    private FileResourceService resourceService;
    @Autowired
    public void setResearchFeatJpaRepository(ResearchFeatJpaRepository repository) {
        this.researchFeatJpaRepository = repository;
    }
    @Override
    public ResearchFeat addOne(ResearchFeat researchFeat) {
        return researchFeatJpaRepository.save(researchFeat);
//        return researchFeat;
    }

    @Override
    public boolean deleteOne(Long id) {
        Optional<ResearchFeat> op = researchFeatJpaRepository.findById(id);
        if (op.isPresent()) {
            researchFeatJpaRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean update(ResearchFeat newFeat) {
        //update the one specified by id
        Optional<ResearchFeat> op = researchFeatJpaRepository.findById(newFeat.getId());
        if (op.isPresent()) {
            researchFeatJpaRepository.save(newFeat);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean addOneResource(Long id, FileResource resource) {
        Optional<ResearchFeat> op = researchFeatJpaRepository.findById(id);
        if (op.isPresent()) {
            op.get().getResources().add(resource);
            researchFeatJpaRepository.save(op.get());
            return true;
        }
        return false;
    }

    @Override
    public boolean addOneResource(Long fid, Long rid) {
        Optional<FileResource> op = resourceService.findById(rid);
        if (op.isPresent()) {
            return addOneResource(fid, op.get());
        }
        return false;
    }

    @Override
    public Optional<ResearchFeat> findById(Long id) {
        return researchFeatJpaRepository.findById(id);
    }

    @Override
    public Page<ResearchFeat> findAllPageable(int page, int size) {
        return researchFeatJpaRepository.findAll(PageRequest.of(page, size, Sort.by("id").ascending()));
    }

    @Override
    public Page<ResearchFeat> findByType(String type, int page, int size) {
        return researchFeatJpaRepository.findByType(type, PageRequest.of(page, size, Sort.by("id").ascending()));
    }

    @Override
    public Page<ResearchFeat> findByLevel(int level, int page, int size) {
        return researchFeatJpaRepository.findByLevel(level, PageRequest.of(page, size, Sort.by("id").ascending()));
    }

    @Override
    public Page<ResearchFeat> findByIsPatent(boolean isPatent, int page, int size) {
        return researchFeatJpaRepository.findByIsPatent(isPatent, PageRequest.of(page, size, Sort.by("id").ascending()));
    }
}
