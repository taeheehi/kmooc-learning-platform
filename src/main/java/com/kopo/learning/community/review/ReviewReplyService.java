package com.kopo.learning.community.review;

import com.kopo.learning.community.vo.Reply;
import com.kopo.learning.community.vo.ReplyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewReplyService {
    private final ReplyMapper replyMapper;

    public List<Reply> getRepliesByPostId(Long postId) {
        return replyMapper.selectRepliesByPostId(postId);
    }

    @Transactional
    public void addReply(Long postId, String userId, String content) {
        Reply reply = Reply.builder()
                .postId(postId)
                .userId(userId)
                .content(content)
                .build();
        replyMapper.insertReply(reply);
    }

    @Transactional
    public void updateReply(Long replyId, String content) {
        Reply reply = Reply.builder()
                .id(replyId)
                .content(content)
                .build();
        replyMapper.updateReply(reply);
    }

    @Transactional
    public void deleteReply(Long replyId) {
        replyMapper.deleteReply(replyId);
    }

    public Reply getReply(Long replyId) {
        return replyMapper.selectReplyById(replyId);
    }

    public List<Reply> getRepliesByPostIdPaged(Long postId, int page, int size) {
        int offset = (page - 1) * size;
        return replyMapper.selectRepliesByPostIdPaged(postId, offset, size);
    }

    public int countRepliesByPostId(Long postId) {
        return replyMapper.countRepliesByPostId(postId);
    }
} 