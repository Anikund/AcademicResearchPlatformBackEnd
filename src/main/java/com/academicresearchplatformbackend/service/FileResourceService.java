package com.academicresearchplatformbackend.service;

import com.academicresearchplatformbackend.dao.FileResource;

public interface FileResourceService {
    public void add(FileResource fileResource);

    public void delete(Long id);

    public void updateName(Long id, String newName);

    public void updateUrl(Long id, String url);
}
