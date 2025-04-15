package com.a5.repository;

import com.a5.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    boolean existsByName(String name);

    @Query("SELECT DISTINCT c FROM Course c LEFT JOIN FETCH c.students")
    Page<Course> findAllWithStudents(Pageable pageable);
}