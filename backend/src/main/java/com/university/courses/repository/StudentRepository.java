package com.university.courses.repository;

import com.university.courses.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    Optional<Student> findByStudentId(String studentId);
    
    Optional<Student> findByEmail(String email);
    
    boolean existsByStudentId(String studentId);
    
    boolean existsByEmail(String email);
    
    List<Student> findByDepartment(String department);
    
    Page<Student> findByDepartmentIgnoreCase(String department, Pageable pageable);
    
    @Query("SELECT s FROM Student s WHERE " +
           "(:name IS NULL OR LOWER(CONCAT(s.firstName, ' ', s.lastName)) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:department IS NULL OR LOWER(s.department) = LOWER(:department)) AND " +
           "(:enrollmentYear IS NULL OR s.enrollmentYear = :enrollmentYear)")
    Page<Student> findStudentsWithFilters(
        @Param("name") String name,
        @Param("department") String department,
        @Param("enrollmentYear") Integer enrollmentYear,
        Pageable pageable
    );
    
    @Query("SELECT DISTINCT s.department FROM Student s ORDER BY s.department")
    List<String> findAllDepartments();
    
    @Query("SELECT DISTINCT s.enrollmentYear FROM Student s WHERE s.enrollmentYear IS NOT NULL ORDER BY s.enrollmentYear DESC")
    List<Integer> findAllEnrollmentYears();
}
