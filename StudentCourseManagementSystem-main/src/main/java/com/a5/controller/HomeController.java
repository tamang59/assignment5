package com.a5.controller;

import com.a5.service.StudentService;
import com.a5.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.transaction.annotation.Transactional;

/**
 * Controller for handling requests to the home page of the application.
 * Provides an overview of key statistics and information.
 */
@Controller
public class HomeController {
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private CourseService courseService;

    @GetMapping("/")
    @Transactional(readOnly = true)
    public String home(Model model) {
        model.addAttribute("totalStudents", studentService.getAllStudents().size());
        model.addAttribute("totalCourses", courseService.getAllCourses().size());
        model.addAttribute("students", studentService.getAllStudents());
        return "home";
    }
} 