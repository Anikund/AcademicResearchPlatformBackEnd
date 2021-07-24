package com.academicresearchplatformbackend.service.Impl;

import com.academicresearchplatformbackend.dao.FileResource;
import com.academicresearchplatformbackend.dao.Lecture;
import com.academicresearchplatformbackend.dao.User;
import com.academicresearchplatformbackend.dao.repository.LectureJpaRepository;
import com.academicresearchplatformbackend.service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LectureServiceImpl implements LectureService {
    private LectureJpaRepository lectureJpaRepository;

    @Autowired
    public void setLectureJpaRepository(LectureJpaRepository repository) {
        this.lectureJpaRepository = repository;
    }
    @Override
    public Lecture addOne(Lecture lecture) {
        if (lectureJpaRepository.findById(lecture.getId()).isPresent()) {
            return lectureJpaRepository.findById(lecture.getId()).get();
        }
        return lectureJpaRepository.save(lecture);
    }

    @Override
    public boolean addResource(Long id, FileResource resource) {
        Optional<Lecture> op = lectureJpaRepository.findById(id);
        if (op.isPresent()) {
            Lecture lecture = op.get();
            lecture.getResources().add(resource);
            lectureJpaRepository.save(lecture);
            return true;
        }
        return false;
    }

    @Override
    public boolean addUser(Long id, User user) {
        Optional<Lecture> op = lectureJpaRepository.findById(id);
        if (op.isPresent()) {
            Lecture lecture = op.get();
            lecture.getUsers().add(user);
            lectureJpaRepository.save(lecture);
            return true;

        }
        return false;
    }

    @Override
    public boolean update(Long id, String field, String value) {
        Optional<Lecture> op = lectureJpaRepository.findById(id);
        if (op.isPresent()) {
            Lecture lecture = op.get();
            switch (field) {
                case "type":
                    lecture.setType(value);
                    break;
                case "description":
                    lecture.setDescription(value);
                    break;
                case "lecturer":
                    lecture.setLecturer(value);
                    break;
                case "fund":
                    lecture.setFund(Integer.parseInt(value));
                    break;
                case "level":
                    lecture.setLevel(value);
                    break;
                case"content":
                    lecture.setContent(value);
                    break;
            }
            lectureJpaRepository.save(lecture);
            return true;

        }
        return false;
    }

    @Override
    public List<Lecture> findAll() {
        return lectureJpaRepository.findAll();
    }

    @Override
    public Page<Lecture> findAllPageable(int page, int size) {
        return lectureJpaRepository.findAll(PageRequest.of(page, size, Sort.by("id").ascending()));
    }

    @Override
    public Page<Lecture> findAllByType(String type, int page, int size) {
        return lectureJpaRepository.findAllByType(type, PageRequest.of(page, size,Sort.by("id").ascending()));
    }

    @Override
    public Page<Lecture> findAllByLevel(String level, int page, int size) {
        return lectureJpaRepository.findAllByLevel(level, PageRequest.of(page, size,Sort.by("id").ascending()));
    }

    @Override
    public boolean deleteById(Long id) {
        if (lectureJpaRepository.findById(id).isPresent()) {

            lectureJpaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Lecture> getById(Long id) {
        return lectureJpaRepository.findById(id);
    }
}
