package com.kopo.learning.student.myclassroom;

import com.kopo.learning.common.vo.LearningRoadmap;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface LearningRoadmapMapper {
    
    // 사용자의 활성 로드맵 조회
    List<LearningRoadmap> findActiveRoadmapByUserId(@Param("userId") String userId);
    
    // 사용자의 모든 로드맵 조회
    List<LearningRoadmap> findAllByUserId(@Param("userId") String userId);
    
    // 로드맵 생성
    int saveRoadmap(LearningRoadmap roadmap);
    
    // 로드맵 단계 생성
    int saveRoadmapStep(LearningRoadmap step);
    
    // 로드맵 업데이트
    int updateRoadmap(LearningRoadmap roadmap);
    
    // 로드맵 단계 업데이트
    int updateRoadmapStep(LearningRoadmap step);
    
    // 로드맵 삭제
    int deleteRoadmap(@Param("id") Long id);
    
    // 로드맵 단계 삭제
    int deleteRoadmapSteps(@Param("roadmapId") Long roadmapId);
    
    // 로드맵 단계별 상태 조회
    List<LearningRoadmap> findRoadmapSteps(@Param("roadmapId") Long roadmapId);
} 