package com.a5.service;

import com.a5.model.Enrollment;
import com.a5.model.Student;
import com.a5.model.Course;
import com.a5.repository.EnrollmentRepository;
import com.a5.repository.StudentRepository;
import com.a5.repository.CourseRepository;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Counter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;


import java.util.List;

@Service
public class EnrollmentService {
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private CourseRepository courseRepository;

    private final Counter enrollmentCounter;
    
    private final Counter unenrollmentCounter;

    public EnrollmentService(MeterRegistry registry) {
        this.enrollmentCounter = Counter.builder("app.enrollments")
                .description("Number of student enrollments")
                .register(registry);
        this.unenrollmentCounter = Counter.builder("app.unenrollments")
                .description("Number of student unenrollments")
                .register(registry);
    }

    
    @Transactional(readOnly = true)
    public Page<Enrollment> getPaginatedEnrollments(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    System.out.println("Fetching all courses from the database...");
    return enrollmentRepository.findAllWithDetails(pageable);
}

    @Cacheable("allEnrollments")
    @Transactional(readOnly = true)
    @Timed(value = "enrollment.list.time", description = "Time taken to list all enrollments")
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAllWithDetails();
    }

    @Transactional(readOnly = true)
    @Timed(value = "enrollment.student.list.time", description = "Time taken to list student enrollments")
    public List<Enrollment> getEnrollmentsByStudentId(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    @Transactional(readOnly = true)
    @Timed(value = "enrollment.course.list.time", description = "Time taken to list course enrollments")
    public List<Enrollment> getEnrollmentsByCourseId(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }

    @Transactional
    @Timed(value = "enrollment.create.time", description = "Time taken to enroll a student")
    public Enrollment enrollStudent(Long studentId, Long courseId) {
        if (enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new IllegalStateException("Student is already enrolled in this course");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        Enrollment enrollment = new Enrollment(student, course);
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        enrollmentCounter.increment();
        return savedEnrollment;
    }

    @Transactional
    @Timed(value = "enrollment.delete.time", description = "Time taken to unenroll a student")
    public void unenrollStudent(Long studentId, Long courseId) {
        enrollmentRepository.findByStudentId(studentId).stream()
                .filter(e -> e.getCourse().getId().equals(courseId))
                .findFirst()
                .ifPresent(enrollment -> {
                    enrollmentRepository.delete(enrollment);
                    unenrollmentCounter.increment();
                });
    }
} 