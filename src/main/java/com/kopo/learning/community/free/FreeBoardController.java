package com.kopo.learning.community.free;

import com.kopo.learning.community.vo.Post;
import com.kopo.learning.common.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/community")
@RequiredArgsConstructor
public class FreeBoardController {

    private final FreeBoardService freeBoardService;

    @GetMapping("/board")
    public String freeBoard(Model model, @RequestParam(defaultValue = "1") int page) {
        final int PAGE_SIZE = 20;
        int total = freeBoardService.countFreeBoardPosts();
        int totalPages = (int) Math.ceil((double) total / PAGE_SIZE);
        List<Post> posts = freeBoardService.getFreeBoardList(page, PAGE_SIZE);
        model.addAttribute("posts", posts);
        model.addAttribute("page", page);
        model.addAttribute("totalPages", totalPages);
        return "community/board";
    }

    @GetMapping("/board/write")
    public String writeForm() {
        return "community/board_write";
    }

    @PostMapping("/board/write")
    public String write(@RequestParam String title, 
                       @RequestParam String content, 
                       @SessionAttribute(name = "user", required = false) User user) {
        if (user == null) {
            return "redirect:/login";
        }
        freeBoardService.createPost(title, content, user);
        return "redirect:/community/board";
    }

    @GetMapping("/board/{idx}")
    public String viewPost(@PathVariable Long idx, Model model) {
        Post post = freeBoardService.getPost(idx);
        freeBoardService.increaseViewCount(idx);
        model.addAttribute("post", post);
        return "community/board_view";
    }

    @GetMapping("/board/{idx}/edit")
    public String editForm(@PathVariable Long idx, 
                          @SessionAttribute(name = "user", required = false) User user,
                          Model model) {
        if (user == null) {
            return "redirect:/login";
        }
        Post post = freeBoardService.getPost(idx);
        if (!post.getAuthorId().equals(user.getIdx())) {
            return "redirect:/community/board";
        }
        model.addAttribute("post", post);
        return "community/board_edit";
    }

    @PostMapping("/board/{idx}/edit")
    public String edit(@PathVariable Long idx,
                      @RequestParam String title,
                      @RequestParam String content,
                      @SessionAttribute(name = "user", required = false) User user) {
        if (user == null) {
            return "redirect:/login";
        }
        Post post = freeBoardService.getPost(idx);
        if (!post.getAuthorId().equals(user.getIdx())) {
            return "redirect:/community/board";
        }
        freeBoardService.updatePost(idx, title, content);
        return "redirect:/community/board/" + idx;
    }

    @PostMapping("/board/{idx}/delete")
    public String delete(@PathVariable Long idx,
                        @SessionAttribute(name = "user", required = false) User user) {
        if (user == null) {
            return "redirect:/login";
        }
        Post post = freeBoardService.getPost(idx);
        if (!post.getAuthorId().equals(user.getIdx())) {
            return "redirect:/community/board";
        }
        freeBoardService.deletePost(idx);
        return "redirect:/community/board";
    }
} 