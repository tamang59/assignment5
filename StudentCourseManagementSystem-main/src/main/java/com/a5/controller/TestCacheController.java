package com.a5.controller;

import com.a5.model.Course;
import com.a5.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestCacheController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/cache")
    public String testCourseCache() {
        courseService.getAllCourses(); // First call – should print
        courseService.getAllCourses(); // Second call – should be silent if cache works
        return "Check your console: If 'Fetching all courses...' printed only once, caching is working!";
    }

    @DeleteMapping("/test/clear-cache")
    public String clearCourseCache() {
        courseService.saveCourse(new Course()); // or use a real evict method
        return "Cache cleared!";
    }

}