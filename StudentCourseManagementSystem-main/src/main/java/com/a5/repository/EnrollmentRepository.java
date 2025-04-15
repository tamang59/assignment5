package com.a5.repository;

import com.a5.model.Enrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    @Query(value = "SELECT e FROM Enrollment e LEFT JOIN FETCH e.student LEFT JOIN FETCH e.course",
           countQuery = "SELECT COUNT(e) FROM Enrollment e")
    Page<Enrollment> findAllWithDetails(Pageable pageable);

    @Query("SELECT e FROM Enrollment e LEFT JOIN FETCH e.student LEFT JOIN FETCH e.course")
    List<Enrollment> findAllWithDetails();

    @Query("SELECT e FROM Enrollment e LEFT JOIN FETCH e.student LEFT JOIN FETCH e.course WHERE e.student.id = :studentId")
    List<Enrollment> findByStudentId(Long studentId);

    @Query("SELECT e FROM Enrollment e LEFT JOIN FETCH e.student LEFT JOIN FETCH e.course WHERE e.course.id = :courseId")
    List<Enrollment> findByCourseId(Long courseId);

    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
}