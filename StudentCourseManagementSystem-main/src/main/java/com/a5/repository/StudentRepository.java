package com.a5.repository;

import com.a5.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByEmail(String email);

    // ✅ Paginated version
    @Query(value = "SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.courses",
           countQuery = "SELECT COUNT(s) FROM Student s")
    Page<Student> findAllWithCourses(Pageable pageable);

    // ✅ Non-paginated version for API, enrollments, dashboard, etc.
    @Query("SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.courses")
    List<Student> findAllWithCourses();

    @Query("SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.courses WHERE s.id = :id")
    Optional<Student> findByIdWithCourses(Long id);
}
