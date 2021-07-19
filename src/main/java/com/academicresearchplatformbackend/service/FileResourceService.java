package com.academicresearchplatformbackend.service;

import com.academicresearchplatformbackend.dao.FileResource;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface FileResourceService {
    FileResource add(FileResource fileResource);

    void delete(Long id);

    void updateName(Long id, String newName);

    void updateUrl(Long id, String url);

    boolean update(FileResource resource);

    Optional<FileResource> findById(Long id);

    Page<FileResource> findAllPageable(int page, int size);
}
