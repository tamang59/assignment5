package com.a5.controller;

import com.a5.model.Enrollment;
import com.a5.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API Controller for managing enrollment resources.
 * Provides endpoints for creating, retrieving, and deleting enrollments between students and courses.
 */
@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentApiController {

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping
    public List<Enrollment> getAllEnrollments() {
        return enrollmentService.getAllEnrollments();
    }

    @GetMapping("/student/{studentId}")
    public List<Enrollment> getEnrollmentsByStudent(@PathVariable Long studentId) {
        return enrollmentService.getEnrollmentsByStudentId(studentId);
    }

    @GetMapping("/course/{courseId}")
    public List<Enrollment> getEnrollmentsByCourse(@PathVariable Long courseId) {
        return enrollmentService.getEnrollmentsByCourseId(courseId);
    }

    @PostMapping
    public Enrollment createEnrollment(@RequestParam Long studentId, @RequestParam Long courseId) {
        return enrollmentService.enrollStudent(studentId, courseId);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteEnrollment(@RequestParam Long studentId, @RequestParam Long courseId) {
        enrollmentService.unenrollStudent(studentId, courseId);
        return ResponseEntity.ok().build();
    }
} 