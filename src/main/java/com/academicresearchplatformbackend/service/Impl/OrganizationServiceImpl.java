package com.academicresearchplatformbackend.service.Impl;

import com.academicresearchplatformbackend.dao.Organization;
import com.academicresearchplatformbackend.dao.ResearchProject;
import com.academicresearchplatformbackend.dao.User;
import com.academicresearchplatformbackend.dao.repository.OrganizationJpaRepository;
import com.academicresearchplatformbackend.service.OrganizationService;
import com.academicresearchplatformbackend.service.UserService;
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
    private UserService userService;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
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

    @Override
    public boolean setPrincipal(Long oid, Long uid) {
        Optional<Organization> op = organizationJpaRepository.findById(oid);
        if (op.isPresent()) {
            Optional<User> opUser = userService.findById(uid);
            if (opUser.isPresent()) {
                op.get().setPrincipal_id(opUser.get().getId());
                organizationJpaRepository.save(op.get());
                return true;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean update(Long id, String fieldName, String newValue) {
        Optional<Organization> op = organizationJpaRepository.findById(id);
        if (op.isPresent()) {
            Organization organization = op.get();
            switch (fieldName) {
                case "name":
                    organization.setName(newValue);
                    break;
                case "location":
                    organization.setLocation(newValue);
                    break;
                case "tel":
                    organization.setTel(newValue);
                    break;
                case "description":
                    organization.setDescription(newValue);
                    break;
                case "regulations":
                    organization.setRegulations(newValue);
                    break;
            }
            organizationJpaRepository.save(organization);
            return true;
        }
        return false;
    }

    @Override
    public Organization addOne(Organization organization) {

        if (organizationJpaRepository.findById(organization.getId()).isPresent()) {
            return organizationJpaRepository.findById(organization.getId()).get();
        } else {
            return organizationJpaRepository.save(organization);
        }
    }

    @Override
    public User getPrincipal(Long oid) {
        Optional<Organization> op = this.getById(oid);
        if (op.isPresent()) {
            Organization organization = op.get();
            Optional<User> opUser = userService.findById(organization.getPrincipal_id());
            if (opUser.isPresent()) {
                return opUser.get();
            }
            return null;
        }
        return null;
    }
}
