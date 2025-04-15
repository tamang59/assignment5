package com.a5.service;

import com.a5.model.Student;
import com.a5.repository.StudentRepository;
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
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Transactional(readOnly = true)
    public Page<Student> getPaginatedStudents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return studentRepository.findAllWithCourses(pageable);
    }

    @Cacheable("allStudents")
    @Transactional(readOnly = true)
    public List<Student> getAllStudents() {
        System.out.println("Fetching all courses from the database...");
        return studentRepository.findAllWithCourses();
    }

    @Transactional(readOnly = true)
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findByIdWithCourses(id);
    }

    @Transactional
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    @Transactional
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public boolean existsByEmail(String email) {
        return studentRepository.existsByEmail(email);
    }
}