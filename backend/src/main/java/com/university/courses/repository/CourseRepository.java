package com.university.courses.repository;

import com.university.courses.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    Optional<Course> findByCode(String code);
    
    boolean existsByCode(String code);
    
    List<Course> findByDepartment(String department);
    
    Page<Course> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    
    Page<Course> findByDepartmentIgnoreCase(String department, Pageable pageable);
    
    @Query("SELECT c FROM Course c WHERE " +
           "(:title IS NULL OR LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
           "(:department IS NULL OR LOWER(c.department) = LOWER(:department)) AND " +
           "(:minCredits IS NULL OR c.credits >= :minCredits) AND " +
           "(:maxCredits IS NULL OR c.credits <= :maxCredits)")
    Page<Course> findCoursesWithFilters(
        @Param("title") String title,
        @Param("department") String department,
        @Param("minCredits") Integer minCredits,
        @Param("maxCredits") Integer maxCredits,
        Pageable pageable
    );
    
    @Query("SELECT DISTINCT c.department FROM Course c ORDER BY c.department")
    List<String> findAllDepartments();
}