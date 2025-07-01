package com.kopo.learning.community.notice;

import com.kopo.learning.community.vo.Post;
import com.kopo.learning.community.vo.PostMapper;
import com.kopo.learning.common.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final PostMapper postMapper;

    public List<Post> getNoticeList(int page, int size) {
        int offset = (page - 1) * size;
        return postMapper.selectPostsByBoardType("NOTICE", size, offset);
    }

    public Post getPost(Long idx) {
        Post post = postMapper.selectPostById(idx);
        if (post == null) {
            throw new RuntimeException("게시글을 찾을 수 없습니다.");
        }
        return post;
    }

    @Transactional
    public void createNotice(String title, String content, User user) {
        Post post = new Post();
        post.setBoardType("NOTICE");
        post.setTitle(title);
        post.setContent(content);
        post.setViewCount(0);
        
        // 작성자 ID는 user.getId()로 저장 (권한 체크용)
        post.setAuthorId(user.getId());
        
        postMapper.insertPost(post);
    }

    @Transactional
    public void updateNotice(Long idx, String title, String content) {
        Post post = new Post();
        post.setIdx(idx);
        post.setTitle(title);
        post.setContent(content);
        postMapper.updatePost(post);
    }

    @Transactional
    public void deleteNotice(Long idx) {
        postMapper.deletePost(idx);
    }

    @Transactional
    public void increaseViewCount(Long idx) {
        postMapper.increaseViewCount(idx);
    }

    public int countNoticePosts() {
        return postMapper.countNoticePosts();
    }
} 