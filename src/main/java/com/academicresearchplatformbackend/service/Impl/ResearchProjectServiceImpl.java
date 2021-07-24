package com.academicresearchplatformbackend.service.Impl;

import com.academicresearchplatformbackend.dao.FileResource;
import com.academicresearchplatformbackend.dao.ResearchFeat;
import com.academicresearchplatformbackend.dao.ResearchProject;
import com.academicresearchplatformbackend.dao.User;
import com.academicresearchplatformbackend.dao.repository.ResearchProjectJpaRepository;
import com.academicresearchplatformbackend.service.FileResourceService;
import com.academicresearchplatformbackend.service.ResearchFeatService;
import com.academicresearchplatformbackend.service.ResearchProjectService;
import com.academicresearchplatformbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class ResearchProjectServiceImpl implements ResearchProjectService {
    private ResearchProjectJpaRepository researchProjectJpaRepository;
    private UserService userService;
    private ResearchFeatService featService;
    private FileResourceService fileResourceService;
    @Autowired
    public void setResearchProjectJpaRepository(ResearchProjectJpaRepository repository) {
        this.researchProjectJpaRepository = repository;
    }

    @Autowired
    public void setFileResourceService(FileResourceService fileResourceService) {
        this.fileResourceService = fileResourceService;
    }
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setFeatService(ResearchFeatService featService) {
        this.featService = featService;
    }


    @Override
    public ResearchProject addOne(ResearchProject project) {
        return researchProjectJpaRepository.save(project);
//        return project;
    }

    @Override
    public boolean addResource(Long id, FileResource resource) {
        Optional<ResearchProject> op = researchProjectJpaRepository.findById(id);
        if (op.isPresent()) {
            op.get().getResources().add(resource);
            researchProjectJpaRepository.save(op.get());
            return true;
        }
        return false;
    }

    @Override
    public boolean addResource(Long id, Long rid) {
        Optional<ResearchProject> op = researchProjectJpaRepository.findById(id);
        if (op.isPresent()) {
//            Optional<User> user = userService.findById(uid);
            Optional<FileResource> resource = fileResourceService.findById(rid);
            if (resource.isPresent()) {
                op.get().getResources().add(resource.get());
                researchProjectJpaRepository.save(op.get());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean addUser(Long pid, Long uid) {
        Optional<ResearchProject> op = researchProjectJpaRepository.findById(pid);
        if (op.isPresent()) {
            Optional<User> user = userService.findById(uid);
            if (user.isPresent()) {
                op.get().getUsers().add(user.get());
                researchProjectJpaRepository.save(op.get());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean addFeat(Long pid, Long fid) {
        Optional<ResearchProject> op = researchProjectJpaRepository.findById(pid);
        if (op.isPresent()) {

            Optional<ResearchFeat> feat = featService.findById(fid);
            if (feat.isPresent()) {
                op.get().getFeats().add(feat.get());
                researchProjectJpaRepository.save(op.get());
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean update(ResearchProject project) {
        Optional<ResearchProject> op = researchProjectJpaRepository.findById(project.getId());
        if (op.isPresent()) {
            researchProjectJpaRepository.save(project);
            return true;
        }
        return false;
    }

    @Override
    public boolean censor(Long id) {
        Optional<ResearchProject> op = researchProjectJpaRepository.findById(id);
        if (op.isPresent()) {
            //researchProjectJpaRepository.save(project);
            op.get().setIsPending(false);
            researchProjectJpaRepository.save(op.get());
            return true;
        }
        return false;
    }

    @Override
    public boolean terminate(Long id) {
        Optional<ResearchProject> op = researchProjectJpaRepository.findById(id);
        if (op.isPresent()) {
            //researchProjectJpaRepository.save(project);
            op.get().setIsTerminated(false);
            researchProjectJpaRepository.save(op.get());
            return true;
        }
        return false;
    }

    @Override
    public int daysToMidwayTime(Long id) {
        Optional<ResearchProject> op = researchProjectJpaRepository.findById(id);
        if (op.isPresent()) {
            //researchProjectJpaRepository.save(project);
            if (op.get().getMidwayTime() == null) {
                return -999;
            }
            return (int) TimeUnit.MILLISECONDS.toDays(
                    System.currentTimeMillis() -
                            op.get().getMidwayTime().getTime()
            );
        }
        return -999;
    }

    @Override
    public int daysToTerminatingTime(Long id) {
        Optional<ResearchProject> op = researchProjectJpaRepository.findById(id);

        if (op.isPresent()) {
            //researchProjectJpaRepository.save(project);
            if (op.get().getTerminatingTime() == null) {
                return -999;
            }
            return (int) TimeUnit.MILLISECONDS.toDays(
                    System.currentTimeMillis() -
                            op.get().getTerminatingTime().getTime()
            );
        }
        return -999;
    }

    @Override
    public Page<ResearchProject> findAllPageable(int page, int size) {
        return researchProjectJpaRepository.findAll(PageRequest.of(page, size, Sort.by("applyTime").descending()));
    }

    @Override
    public List<ResearchProject> findAll() {
        return researchProjectJpaRepository.findAll();
    }

    @Override
    public Optional<ResearchProject> findById(Long id) {
        return researchProjectJpaRepository.findById(id);
    }

    @Override
    public boolean assignFund(Long pid, int amount) {
        Optional<ResearchProject> op = researchProjectJpaRepository.findById(pid);
        if (op.isPresent()) {
            if (!op.get().isWaitingFund()) {
                return false;
            }
            if (op.get().isWaitingFund()) {
                op.get().setFund(amount);
                op.get().setLeftFund(amount);
                op.get().setWaitingFund(false);
                researchProjectJpaRepository.save(op.get());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean consumeFund(Long pid, int amount) {
        Optional<ResearchProject> op = researchProjectJpaRepository.findById(pid);
        if (op.isPresent()) {
            ResearchProject project = op.get();
            if (project.getLeftFund() >= amount) {
                project.setCost(project.getCost() + amount);
                project.setLeftFund(project.getLeftFund() - amount);
                researchProjectJpaRepository.save(project);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean midExamine(Long pid) {
        Optional<ResearchProject> op = researchProjectJpaRepository.findById(pid);
        if (op.isPresent()) {
            ResearchProject project = op.get();
            if (project.getIsMidExaminedSuccess()) {
                return false;
            } else {
                op.get().setIsMidExaminedSuccess(true);
                researchProjectJpaRepository.save(op.get());
            }
        }
        return false;
    }

    @Override
    public ResearchProject deleteById(Long id) {
        Optional<ResearchProject> op = researchProjectJpaRepository.findById(id);
        if (op.isPresent()) {
            researchProjectJpaRepository.deleteById(id);
            return op.get();
        }
        return null;
    }


}
