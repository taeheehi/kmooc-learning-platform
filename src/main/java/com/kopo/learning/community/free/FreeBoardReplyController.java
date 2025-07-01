package com.kopo.learning.community.free;

import com.kopo.learning.community.vo.Reply;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/free/reply")
@RequiredArgsConstructor
public class FreeBoardReplyController {
    private final FreeBoardReplyService replyService;
    private static final int PAGE_SIZE = 30;

    // 댓글 목록 (페이징)
    @GetMapping("/list")
    public Map<String, Object> list(@RequestParam Long postId,
                                    @RequestParam(defaultValue = "1") int page) {
        int total = replyService.countRepliesByPostId(postId);
        List<Reply> replies = replyService.getRepliesByPostIdPaged(postId, page, PAGE_SIZE);
        int totalPages = (int) Math.ceil((double) total / PAGE_SIZE);
        Map<String, Object> result = new HashMap<>();
        result.put("replies", replies);
        result.put("total", total);
        result.put("page", page);
        result.put("totalPages", totalPages);
        return result;
    }

    // 댓글 등록
    @PostMapping("/add")
    public Map<String, Object> add(@RequestParam Long postId,
                                   @RequestParam String content,
                                   HttpSession session) {
        com.kopo.learning.common.vo.User user = (com.kopo.learning.common.vo.User) session.getAttribute("user");
        String userId = user != null ? user.getId() : null;
        Map<String, Object> result = new HashMap<>();
        if (userId == null) {
            result.put("success", false);
            result.put("message", "로그인이 필요합니다.");
            return result;
        }
        replyService.addReply(postId, userId, content);
        result.put("success", true);
        return result;
    }

    // 댓글 수정
    @PostMapping("/update")
    public Map<String, Object> update(@RequestParam Long replyId,
                                      @RequestParam String content,
                                      HttpSession session) {
        com.kopo.learning.common.vo.User user = (com.kopo.learning.common.vo.User) session.getAttribute("user");
        String userId = user != null ? user.getId() : null;
        Map<String, Object> result = new HashMap<>();
        Reply reply = replyService.getReply(replyId);
        if (userId == null || reply == null || !userId.equals(reply.getUserId())) {
            result.put("success", false);
            result.put("message", "본인 댓글만 수정할 수 있습니다.");
            return result;
        }
        replyService.updateReply(replyId, content);
        result.put("success", true);
        return result;
    }

    // 댓글 삭제
    @PostMapping("/delete")
    public Map<String, Object> delete(@RequestParam Long replyId,
                                      HttpSession session) {
        com.kopo.learning.common.vo.User user = (com.kopo.learning.common.vo.User) session.getAttribute("user");
        String userId = user != null ? user.getId() : null;
        Map<String, Object> result = new HashMap<>();
        Reply reply = replyService.getReply(replyId);
        if (userId == null || reply == null || !userId.equals(reply.getUserId())) {
            result.put("success", false);
            result.put("message", "본인 댓글만 삭제할 수 있습니다.");
            return result;
        }
        replyService.deleteReply(replyId);
        result.put("success", true);
        return result;
    }
} 