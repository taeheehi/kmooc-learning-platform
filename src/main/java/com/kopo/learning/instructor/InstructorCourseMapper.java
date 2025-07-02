package com.kopo.learning.instructor;

import com.kopo.learning.common.vo.InstructorCourse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface InstructorCourseMapper {
    
    // 강의자의 모든 강의 조회
    List<InstructorCourse> findByInstructorId(@Param("instructorId") String instructorId);
    
    // 모든 강의 조회 (관리자용)
    List<InstructorCourse> findAll();
    
    // 특정 상태의 모든 강의 조회 (관리자용)
    List<InstructorCourse> findByStatus(@Param("status") String status);
    
    // 강의자의 특정 상태 강의 조회
    List<InstructorCourse> findByInstructorIdAndStatus(@Param("instructorId") String instructorId, @Param("status") String status);
    
    // 강의 상세 조회
    InstructorCourse findById(@Param("id") Long id);
    
    // 강의 등록
    int save(InstructorCourse course);
    
    // 강의 수정
    int update(InstructorCourse course);
    
    // 강의 삭제
    int delete(@Param("id") Long id);
    
    // 강의 상태 변경
    int updateStatus(@Param("id") Long id, @Param("status") String status);
    
    // 강의별 수강생 수 조회
    int countEnrollmentsByCourseId(@Param("courseId") Long courseId);
    
    // 강의별 평균 평점 조회
    Double getAverageRatingByCourseId(@Param("courseId") Long courseId);
    
    // 강의별 리뷰 수 조회
    int countReviewsByCourseId(@Param("courseId") Long courseId);
} 