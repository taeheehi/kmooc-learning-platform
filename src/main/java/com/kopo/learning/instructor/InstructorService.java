package com.kopo.learning.instructor;

import com.kopo.learning.common.vo.InstructorCourse;
import com.kopo.learning.common.vo.Enrollment;
import com.kopo.learning.common.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class InstructorService {
    
    @Autowired
    private InstructorCourseMapper instructorCourseMapper;
    
    @Autowired
    private com.kopo.learning.student.myclassroom.EnrollmentMapper enrollmentMapper;
    
    // ========== 강의 관리 메서드 ==========
    
    /**
     * 강의자의 모든 강의 조회
     */
    public List<InstructorCourse> getInstructorCourses(String instructorId) {
        return instructorCourseMapper.findByInstructorId(instructorId);
    }
    
    /**
     * 모든 강의 조회 (관리자용)
     */
    public List<InstructorCourse> getAllCourses() {
        return instructorCourseMapper.findAll();
    }
    
    /**
     * 강의자의 특정 상태 강의 조회
     */
    public List<InstructorCourse> getInstructorCoursesByStatus(String instructorId, String status) {
        return instructorCourseMapper.findByInstructorIdAndStatus(instructorId, status);
    }
    
    /**
     * 강의 상세 조회
     */
    public InstructorCourse getCourseById(Long courseId) {
        return instructorCourseMapper.findById(courseId);
    }
    
    /**
     * 강의 등록
     */
    @Transactional
    public boolean createCourse(InstructorCourse course) {
        course.setCreatedAt(LocalDateTime.now());
        course.setUpdatedAt(LocalDateTime.now());
        if (course.getStatus() == null) {
            course.setStatus("DRAFT");
        }
        
        return instructorCourseMapper.save(course) > 0;
    }
    
    /**
     * 강의 수정
     */
    @Transactional
    public boolean updateCourse(InstructorCourse course) {
        course.setUpdatedAt(LocalDateTime.now());
        return instructorCourseMapper.update(course) > 0;
    }
    
    /**
     * 강의 삭제
     */
    @Transactional
    public boolean deleteCourse(Long courseId) {
        return instructorCourseMapper.delete(courseId) > 0;
    }
    
    /**
     * 강의 상태 변경
     */
    @Transactional
    public boolean updateCourseStatus(Long courseId, String status) {
        return instructorCourseMapper.updateStatus(courseId, status) > 0;
    }
    
    // ========== 수강생 관리 메서드 ==========
    
    /**
     * 강의별 수강생 목록 조회
     */
    public List<Enrollment> getCourseEnrollments(Long courseId) {
        // EnrollmentMapper에 강의별 수강생 조회 메서드 추가 필요
        return null; // TODO: 구현 필요
    }
    
    /**
     * 강의별 수강생 통계
     */
    public Map<String, Object> getCourseEnrollmentStats(Long courseId) {
        Map<String, Object> stats = new HashMap<>();
        
        // 전체 수강생 수
        int totalEnrollments = instructorCourseMapper.countEnrollmentsByCourseId(courseId);
        stats.put("totalEnrollments", totalEnrollments);
        
        // 평균 평점
        double averageRating = instructorCourseMapper.getAverageRatingByCourseId(courseId);
        stats.put("averageRating", Math.round(averageRating * 10) / 10.0);
        
        // 리뷰 수
        int reviewCount = instructorCourseMapper.countReviewsByCourseId(courseId);
        stats.put("reviewCount", reviewCount);
        
        return stats;
    }
    
    // ========== 대시보드 메서드 ==========
    
    /**
     * 강의자 대시보드 통계
     */
    public Map<String, Object> getInstructorDashboardStats(String instructorId) {
        Map<String, Object> stats = new HashMap<>();
        
        // 전체 강의 수
        List<InstructorCourse> allCourses = instructorCourseMapper.findByInstructorId(instructorId);
        stats.put("totalCourses", allCourses.size());
        
        // 공개된 강의 수
        List<InstructorCourse> publishedCourses = instructorCourseMapper.findByInstructorIdAndStatus(instructorId, "PUBLISHED");
        stats.put("publishedCourses", publishedCourses.size());
        
        // 임시저장 강의 수
        List<InstructorCourse> draftCourses = instructorCourseMapper.findByInstructorIdAndStatus(instructorId, "DRAFT");
        stats.put("draftCourses", draftCourses.size());
        
        // 총 수강생 수
        int totalEnrollments = 0;
        for (InstructorCourse course : allCourses) {
            totalEnrollments += course.getEnrollmentCount() != null ? course.getEnrollmentCount() : 0;
        }
        stats.put("totalEnrollments", totalEnrollments);
        
        // 평균 평점
        double totalRating = 0;
        int courseWithRating = 0;
        for (InstructorCourse course : allCourses) {
            if (course.getAverageRating() != null && course.getAverageRating() > 0) {
                totalRating += course.getAverageRating();
                courseWithRating++;
            }
        }
        double averageRating = courseWithRating > 0 ? totalRating / courseWithRating : 0;
        stats.put("averageRating", Math.round(averageRating * 10) / 10.0);
        
        return stats;
    }
    
    /**
     * 관리자 대시보드 통계
     */
    public Map<String, Object> getAdminDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 전체 강의 수
        List<InstructorCourse> allCourses = instructorCourseMapper.findAll();
        stats.put("totalCourses", allCourses.size());
        
        // 공개된 강의 수
        List<InstructorCourse> publishedCourses = instructorCourseMapper.findByStatus("PUBLISHED");
        stats.put("publishedCourses", publishedCourses.size());
        
        // 임시저장 강의 수
        List<InstructorCourse> draftCourses = instructorCourseMapper.findByStatus("DRAFT");
        stats.put("draftCourses", draftCourses.size());
        
        // 총 수강생 수
        int totalEnrollments = 0;
        for (InstructorCourse course : allCourses) {
            totalEnrollments += course.getEnrollmentCount() != null ? course.getEnrollmentCount() : 0;
        }
        stats.put("totalEnrollments", totalEnrollments);
        
        // 평균 평점
        double totalRating = 0;
        int courseWithRating = 0;
        for (InstructorCourse course : allCourses) {
            if (course.getAverageRating() != null && course.getAverageRating() > 0) {
                totalRating += course.getAverageRating();
                courseWithRating++;
            }
        }
        double averageRating = courseWithRating > 0 ? totalRating / courseWithRating : 0;
        stats.put("averageRating", Math.round(averageRating * 10) / 10.0);
        
        return stats;
    }
} 