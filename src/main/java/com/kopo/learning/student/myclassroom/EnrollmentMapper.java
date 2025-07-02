package com.kopo.learning.student.myclassroom;

import com.kopo.learning.common.vo.Enrollment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface EnrollmentMapper {
    
    // 사용자의 모든 수강 정보 조회
    List<Enrollment> findByUserId(@Param("userId") String userId);
    
    // 사용자의 특정 상태 수강 정보 조회
    List<Enrollment> findByUserIdAndStatus(@Param("userId") String userId, @Param("status") String status);
    
    // 수강 신청
    int save(Enrollment enrollment);
    
    // 수강 정보 업데이트
    int update(Enrollment enrollment);
    
    // 진행률 업데이트
    int updateProgress(@Param("id") Long id, @Param("progress") Integer progress);
    
    // 상태 업데이트
    int updateStatus(@Param("id") Long id, @Param("status") String status);
    
    // 마지막 접속 시간 업데이트
    int updateLastAccessedAt(@Param("id") Long id);
    
    // 특정 강의 수강 여부 확인
    Enrollment findByUserIdAndCourseId(@Param("userId") String userId, @Param("courseId") Long courseId);
    
    // 수강 정보 삭제
    int delete(@Param("id") Long id);
} 