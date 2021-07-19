package com.academicresearchplatformbackend.service.Impl;

import com.academicresearchplatformbackend.dao.Organization;
import com.academicresearchplatformbackend.dao.ResearchProject;
import com.academicresearchplatformbackend.dao.repository.OrganizationJpaRepository;
import com.academicresearchplatformbackend.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class OrganizationServiceImpl implements OrganizationService {
    private OrganizationJpaRepository organizationJpaRepository;
    @Autowired
    public void setOrganizationJpaRepository(OrganizationJpaRepository repo) {
        this.organizationJpaRepository = repo;
    }
//    @Override
//    public Organization getOrganizationByProjectId(Long id) {
//        List<Organization> allOrg = organizationJpaRepository.findAll();
//        for (Organization org:allOrg
//             ) {
//            List<ResearchProject> projects = org.getProjects();
//            for (ResearchProject project:projects
//                 ) {
//                if (project.getId() == id) {
//                    return org;
//                }
//            }
//        }
//        return null;
//
//    }

    @Override
    public Page<Organization> getAllPageable(int page, int size) {
        return organizationJpaRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Optional<Organization> getById(Long id) {
        return organizationJpaRepository.findById(id);
    }

    @Override
    public Optional<Organization> getByName(String name) {
        return organizationJpaRepository.findByName(name);
    }
}
