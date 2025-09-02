package com.university.courses.repository;

import com.university.courses.entity.Registration;
import com.university.courses.entity.Registration.RegistrationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    
    Optional<Registration> findByStudentIdAndCourseId(Long studentId, Long courseId);
    
    List<Registration> findByStudentId(Long studentId);
    
    List<Registration> findByCourseId(Long courseId);
    
    List<Registration> findByStatus(RegistrationStatus status);
    
    Page<Registration> findByStudentId(Long studentId, Pageable pageable);
    
    Page<Registration> findByCourseId(Long courseId, Pageable pageable);
    
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
    
    @Query("SELECT COUNT(r) FROM Registration r WHERE r.course.id = :courseId AND r.status = :status")
    long countByCourseIdAndStatus(@Param("courseId") Long courseId, @Param("status") RegistrationStatus status);
    
    @Query("SELECT r FROM Registration r JOIN FETCH r.student JOIN FETCH r.course WHERE r.student.id = :studentId")
    List<Registration> findByStudentIdWithDetails(@Param("studentId") Long studentId);
    
    @Query("SELECT r FROM Registration r JOIN FETCH r.student JOIN FETCH r.course WHERE r.course.id = :courseId")
    List<Registration> findByCourseIdWithDetails(@Param("courseId") Long courseId);
}

