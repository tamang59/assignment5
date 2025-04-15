package com.a5.service;

import com.a5.model.Course;
import com.a5.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;


import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Transactional(readOnly = true)
    public Page<Course> getPaginatedCourses(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return courseRepository.findAllWithStudents(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    @Transactional
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    @Transactional
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public boolean existsByName(String name) {
        return courseRepository.existsByName(name);
    }

    @Cacheable("allCourses")
    @Transactional(readOnly = true)
    public List<Course> getAllCourses() {
        System.out.println("Fetching all courses from the database...");
        return courseRepository.findAll();
    }
}