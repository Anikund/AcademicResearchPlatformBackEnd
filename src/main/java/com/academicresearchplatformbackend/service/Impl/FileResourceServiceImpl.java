package com.academicresearchplatformbackend.service.Impl;

import com.academicresearchplatformbackend.dao.FileResource;
import com.academicresearchplatformbackend.dao.repository.FileResourceJpaRepository;
import com.academicresearchplatformbackend.service.FileResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Optional;

@Service

public class FileResourceServiceImpl implements FileResourceService {
    private FileResourceJpaRepository fileResourceJpaRepository;

    @Autowired
    public void setFileResourceJpaRepository(FileResourceJpaRepository repository) {
        this.fileResourceJpaRepository = repository;
    }
    @Override
    public FileResource add(FileResource fileResource) {
        return fileResourceJpaRepository.save(fileResource);
    }

    @Override
    public void delete(Long id) {
        fileResourceJpaRepository.deleteById(id);

    }

    @Override
    public void updateName(Long id, String newName) {
        Optional<FileResource> op = fileResourceJpaRepository.findById(id);
        if (op.isPresent()) {
            FileResource r = op.get();
            r.setName(newName);
            fileResourceJpaRepository.save(r);
        }
    }

    @Override
    public void updateUrl(Long id, String url) {
        Optional<FileResource> op = fileResourceJpaRepository.findById(id);
        if (op.isPresent()) {
            FileResource r = op.get();
            r.setUrl(url);
            fileResourceJpaRepository.save(r);
        }
    }

    @Override
    public boolean update(FileResource resource) {
        Optional<FileResource> op = fileResourceJpaRepository.findById(resource.getId());
        if (op.isPresent()) {
            fileResourceJpaRepository.save(resource);
            return true;
        }
        return false;
    }

    @Override
    public Optional<FileResource> findById(Long id) {
        return fileResourceJpaRepository.findById(id);
    }

    @Override
    public Page<FileResource> findAllPageable(int page, int size) {
        return fileResourceJpaRepository.findAll(PageRequest.of(page, size));
    }
}
