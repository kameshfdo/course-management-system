package com.university.courses.repository;

import com.university.courses.entity.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    
    Optional<Result> findByRegistrationId(Long registrationId);
    
    @Query("SELECT r FROM Result r JOIN r.registration reg WHERE reg.student.id = :studentId")
    List<Result> findByStudentId(@Param("studentId") Long studentId);
    
    @Query("SELECT r FROM Result r JOIN r.registration reg WHERE reg.course.id = :courseId")
    List<Result> findByCourseId(@Param("courseId") Long courseId);
    
    @Query("SELECT r FROM Result r JOIN FETCH r.registration reg JOIN FETCH reg.student JOIN FETCH reg.course WHERE reg.student.id = :studentId")
    List<Result> findByStudentIdWithDetails(@Param("studentId") Long studentId);
    
    @Query("SELECT r FROM Result r JOIN FETCH r.registration reg JOIN FETCH reg.student JOIN FETCH reg.course WHERE reg.course.id = :courseId")
    List<Result> findByCourseIdWithDetails(@Param("courseId") Long courseId);
    
    @Query("SELECT AVG(r.marks) FROM Result r JOIN r.registration reg WHERE reg.course.id = :courseId")
    BigDecimal findAverageMarksByCourseId(@Param("courseId") Long courseId);
    
    @Query("SELECT AVG(r.gpaPoints) FROM Result r JOIN r.registration reg WHERE reg.student.id = :studentId")
    BigDecimal findAverageGPAByStudentId(@Param("studentId") Long studentId);
    
    @Query("SELECT r FROM Result r WHERE r.marks >= :minMarks AND r.marks <= :maxMarks")
    Page<Result> findByMarksRange(@Param("minMarks") BigDecimal minMarks, @Param("maxMarks") BigDecimal maxMarks, Pageable pageable);
}
