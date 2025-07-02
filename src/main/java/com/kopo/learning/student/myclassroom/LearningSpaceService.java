package com.kopo.learning.student.myclassroom;

import com.kopo.learning.common.vo.Enrollment;
import com.kopo.learning.common.vo.Wishlist;
import com.kopo.learning.common.vo.LearningRoadmap;
import com.kopo.learning.common.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class LearningSpaceService {
    
    @Autowired
    private EnrollmentMapper enrollmentMapper;
    
    @Autowired
    private WishlistMapper wishlistMapper;
    
    @Autowired
    private LearningRoadmapMapper learningRoadmapMapper;
    
    // ========== 수강 관련 메서드 ==========
    
    /**
     * 사용자의 모든 수강 정보 조회
     */
    public List<Enrollment> getUserEnrollments(String userId) {
        return enrollmentMapper.findByUserId(userId);
    }
    
    /**
     * 사용자의 특정 상태 수강 정보 조회
     */
    public List<Enrollment> getUserEnrollmentsByStatus(String userId, String status) {
        return enrollmentMapper.findByUserIdAndStatus(userId, status);
    }
    
    /**
     * 수강 신청
     */
    @Transactional
    public boolean enrollCourse(String userId, Long courseId) {
        // 이미 수강 중인지 확인
        Enrollment existing = enrollmentMapper.findByUserIdAndCourseId(userId, courseId);
        if (existing != null) {
            return false; // 이미 수강 중
        }
        
        Enrollment enrollment = Enrollment.builder()
                .userId(userId)
                .courseId(courseId)
                .status("ENROLLED")
                .progress(0)
                .enrolledAt(LocalDateTime.now())
                .lastAccessedAt(LocalDateTime.now())
                .build();
        
        return enrollmentMapper.save(enrollment) > 0;
    }
    
    /**
     * 학습 시작
     */
    @Transactional
    public boolean startLearning(Long enrollmentId) {
        Enrollment enrollment = new Enrollment();
        enrollment.setId(enrollmentId);
        enrollment.setStatus("IN_PROGRESS");
        enrollment.setStartedAt(LocalDateTime.now());
        enrollment.setLastAccessedAt(LocalDateTime.now());
        
        return enrollmentMapper.update(enrollment) > 0;
    }
    
    /**
     * 진행률 업데이트
     */
    @Transactional
    public boolean updateProgress(Long enrollmentId, Integer progress) {
        if (progress >= 100) {
            // 100% 완료 시 수료 처리
            Enrollment enrollment = new Enrollment();
            enrollment.setId(enrollmentId);
            enrollment.setStatus("COMPLETED");
            enrollment.setCompletedAt(LocalDateTime.now());
            enrollment.setLastAccessedAt(LocalDateTime.now());
            return enrollmentMapper.update(enrollment) > 0;
        } else {
            return enrollmentMapper.updateProgress(enrollmentId, progress) > 0;
        }
    }
    
    /**
     * 수강 취소
     */
    @Transactional
    public boolean dropCourse(Long enrollmentId) {
        return enrollmentMapper.updateStatus(enrollmentId, "DROPPED") > 0;
    }
    
    // ========== 찜한 강의 관련 메서드 ==========
    
    /**
     * 사용자의 찜한 강의 목록 조회
     */
    public List<Wishlist> getUserWishlist(String userId) {
        return wishlistMapper.findByUserId(userId);
    }
    
    /**
     * 강의 찜하기
     */
    @Transactional
    public boolean addToWishlist(String userId, Long courseId) {
        // 이미 찜한 강의인지 확인
        Wishlist existing = wishlistMapper.findByUserIdAndCourseId(userId, courseId);
        if (existing != null) {
            return false; // 이미 찜한 강의
        }
        
        Wishlist wishlist = Wishlist.builder()
                .userId(userId)
                .courseId(courseId)
                .addedAt(LocalDateTime.now())
                .build();
        
        return wishlistMapper.save(wishlist) > 0;
    }
    
    /**
     * 찜한 강의 삭제
     */
    @Transactional
    public boolean removeFromWishlist(Long wishlistId) {
        return wishlistMapper.delete(wishlistId) > 0;
    }
    
    /**
     * 찜한 강의에서 수강 신청
     */
    @Transactional
    public boolean enrollFromWishlist(String userId, Long courseId) {
        // 찜한 강의 삭제
        Wishlist wishlist = wishlistMapper.findByUserIdAndCourseId(userId, courseId);
        if (wishlist != null) {
            wishlistMapper.delete(wishlist.getId());
        }
        
        // 수강 신청
        return enrollCourse(userId, courseId);
    }
    
    // ========== 학습 로드맵 관련 메서드 ==========
    
    /**
     * 사용자의 활성 로드맵 조회
     */
    public List<LearningRoadmap> getUserActiveRoadmap(String userId) {
        return learningRoadmapMapper.findActiveRoadmapByUserId(userId);
    }
    
    /**
     * 로드맵 생성
     */
    @Transactional
    public boolean createRoadmap(String userId, String roadmapName, String description, 
                               List<Long> courseIds) {
        // 기존 활성 로드맵 비활성화
        List<LearningRoadmap> existingRoadmaps = learningRoadmapMapper.findAllByUserId(userId);
        for (LearningRoadmap existing : existingRoadmaps) {
            if ("ACTIVE".equals(existing.getStatus())) {
                LearningRoadmap updateRoadmap = new LearningRoadmap();
                updateRoadmap.setId(existing.getId());
                updateRoadmap.setStatus("INACTIVE");
                updateRoadmap.setUpdatedAt(LocalDateTime.now());
                learningRoadmapMapper.updateRoadmap(updateRoadmap);
            }
        }
        
        // 새 로드맵 생성
        LearningRoadmap roadmap = LearningRoadmap.builder()
                .userId(userId)
                .roadmapName(roadmapName)
                .description(description)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        int result = learningRoadmapMapper.saveRoadmap(roadmap);
        if (result <= 0) return false;
        
        // 로드맵 단계 생성
        for (int i = 0; i < courseIds.size(); i++) {
            LearningRoadmap step = LearningRoadmap.builder()
                    .id(roadmap.getId())
                    .stepOrder(i + 1)
                    .courseId(courseIds.get(i))
                    .stepStatus("NOT_STARTED")
                    .build();
            
            learningRoadmapMapper.saveRoadmapStep(step);
        }
        
        return true;
    }
    
    /**
     * 로드맵 단계 상태 업데이트
     */
    @Transactional
    public boolean updateRoadmapStepStatus(Long stepId, String status) {
        LearningRoadmap step = new LearningRoadmap();
        step.setStepId(stepId);
        step.setStepStatus(status);
        
        return learningRoadmapMapper.updateRoadmapStep(step) > 0;
    }
    
    // ========== 통계 및 대시보드 메서드 ==========
    
    /**
     * 사용자 학습 통계 조회
     */
    public Map<String, Object> getUserLearningStats(String userId) {
        Map<String, Object> stats = new HashMap<>();
        
        // 전체 수강 강의 수
        List<Enrollment> allEnrollments = enrollmentMapper.findByUserId(userId);
        stats.put("totalEnrollments", allEnrollments.size());
        
        // 진행 중인 강의 수
        List<Enrollment> inProgress = enrollmentMapper.findByUserIdAndStatus(userId, "IN_PROGRESS");
        stats.put("inProgressCount", inProgress.size());
        
        // 완료된 강의 수
        List<Enrollment> completed = enrollmentMapper.findByUserIdAndStatus(userId, "COMPLETED");
        stats.put("completedCount", completed.size());
        
        // 찜한 강의 수
        int wishlistCount = wishlistMapper.countByUserId(userId);
        stats.put("wishlistCount", wishlistCount);
        
        // 평균 진행률
        double avgProgress = allEnrollments.stream()
                .mapToInt(Enrollment::getProgress)
                .average()
                .orElse(0.0);
        stats.put("averageProgress", Math.round(avgProgress * 10) / 10.0);
        
        return stats;
    }
} 