package com.kopo.learning.community.free;

import com.kopo.learning.community.vo.Post;
import com.kopo.learning.community.vo.PostMapper;
import com.kopo.learning.common.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FreeBoardService {

    private final PostMapper postMapper;

    public List<Post> getFreeBoardList(int page, int size) {
        int offset = (page - 1) * size;
        return postMapper.selectFreeBoardListWithReplyCount(size, offset);
    }

    public Post getPost(Long idx) {
        Post post = postMapper.selectPostById(idx);
        if (post == null) {
            throw new RuntimeException("게시글을 찾을 수 없습니다.");
        }
        return post;
    }

    @Transactional
    public void createPost(String title, String content, User user) {
        Post post = new Post();
        post.setBoardType("FREE");
        post.setTitle(title);
        post.setContent(content);
        post.setAuthorId(user.getId());
        post.setViewCount(0);
        postMapper.insertPost(post);
    }

    @Transactional
    public void updatePost(Long idx, String title, String content) {
        Post post = new Post();
        post.setIdx(idx);
        post.setTitle(title);
        post.setContent(content);
        postMapper.updatePost(post);
    }

    @Transactional
    public void deletePost(Long idx) {
        postMapper.deletePost(idx);
    }

    @Transactional
    public void increaseViewCount(Long idx) {
        postMapper.increaseViewCount(idx);
    }

    public int countFreeBoardPosts() {
        return postMapper.countFreeBoardPosts();
    }
} 