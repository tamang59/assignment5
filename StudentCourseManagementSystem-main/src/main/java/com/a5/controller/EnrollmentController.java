package com.a5.controller;

import com.a5.service.StudentService;
import com.a5.model.Enrollment;
import com.a5.service.CourseService;
import com.a5.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;


@Controller
@RequestMapping("/enrollments")
public class EnrollmentController {
    private static final Logger logger = LoggerFactory.getLogger(EnrollmentController.class);
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping
    public String listEnrollments(@RequestParam(defaultValue = "0") int page, Model model) {
    int pageSize = 3; // show 3 enrollments per page
    var students = studentService.getAllStudents();
    var courses = courseService.getAllCourses();
    var enrollmentsPage = enrollmentService.getPaginatedEnrollments(page, pageSize);

    model.addAttribute("students", students);
    model.addAttribute("courses", courses);
    model.addAttribute("enrollments", enrollmentsPage.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", enrollmentsPage.getTotalPages());

    return "enrollments/list";
    }

    @GetMapping("/report/pdf")
    public void generatePdfReport(HttpServletResponse response) throws IOException {
    response.setContentType("application/pdf");
    response.setHeader("Content-Disposition", "attachment; filename=enrollments-report.pdf");

    List<Enrollment> enrollments = enrollmentService.getAllEnrollments();

    Document document = new Document(PageSize.A4);
    PdfWriter.getInstance(document, response.getOutputStream());

    document.open();

    Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
    Paragraph title = new Paragraph("Student Enrollments Report", titleFont);
    title.setAlignment(Paragraph.ALIGN_CENTER);
    title.setSpacingAfter(20);
    document.add(title);

    PdfPTable table = new PdfPTable(4);
    table.setWidthPercentage(100);
    table.setSpacingBefore(10);
    table.addCell("Student ID");
    table.addCell("Student Name");
    table.addCell("Course Name");
    table.addCell("Enrollment Date");

    for (Enrollment enrollment : enrollments) {
        table.addCell(String.valueOf(enrollment.getStudent().getId()));
        table.addCell(enrollment.getStudent().getName());
        table.addCell(enrollment.getCourse().getName());
        table.addCell(enrollment.getEnrollmentDate().toString());
    }

    document.add(table);
    document.close();
    }

    @PostMapping("/enroll")
    public String enrollStudent(@RequestParam Long studentId, @RequestParam Long courseId, Model model) {
        logger.info("Enrolling student {} in course {}", studentId, courseId);
        
        try {
            enrollmentService.enrollStudent(studentId, courseId);
            logger.info("Successfully enrolled student {} in course {}", studentId, courseId);
            return "redirect:/enrollments";
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("students", studentService.getAllStudents());
            model.addAttribute("courses", courseService.getAllCourses());
            model.addAttribute("enrollments", enrollmentService.getAllEnrollments());
            return "enrollments/list";
        }
    }

    @PostMapping("/unenroll")
    public String unenrollStudent(@RequestParam Long studentId, @RequestParam Long courseId) {
        logger.info("Unenrolling student {} from course {}", studentId, courseId);
        enrollmentService.unenrollStudent(studentId, courseId);
        logger.info("Successfully unenrolled student {} from course {}", studentId, courseId);
        return "redirect:/enrollments";
    }
} 