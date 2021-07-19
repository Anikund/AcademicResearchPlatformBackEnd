package com.academicresearchplatformbackend.service;

import com.academicresearchplatformbackend.dao.Organization;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface OrganizationService {
//    Organization getOrganizationByProjectId(Long id);

    Page<Organization> getAllPageable(int page, int size);

    Optional<Organization> getById(Long id);

    Optional<Organization> getByName(String name);
}
