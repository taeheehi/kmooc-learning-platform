package com.kopo.learning.community.question;

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
public class QnaController {
    private final QnaService qnaService;

    @GetMapping("/qna")
    public String qnaBoard(Model model, @RequestParam(defaultValue = "1") int page) {
        final int PAGE_SIZE = 20;
        int total = qnaService.countQnaPosts();
        int totalPages = (int) Math.ceil((double) total / PAGE_SIZE);
        List<Post> posts = qnaService.getQnaList(page, PAGE_SIZE);
        model.addAttribute("posts", posts);
        model.addAttribute("page", page);
        model.addAttribute("totalPages", totalPages);
        return "community/qna";
    }

    @GetMapping("/qna/write")
    public String writeForm() {
        return "community/qna_write";
    }

    @PostMapping("/qna/write")
    public String write(@RequestParam String title, @RequestParam String content, @SessionAttribute(name = "user", required = false) User user) {
        if (user == null) return "redirect:/login";
        qnaService.createPost(title, content, user);
        return "redirect:/community/qna";
    }

    @GetMapping("/qna/{idx}")
    public String viewPost(@PathVariable Long idx, Model model) {
        Post post = qnaService.getPost(idx);
        qnaService.increaseViewCount(idx);
        model.addAttribute("post", post);
        return "community/qna_view";
    }

    @GetMapping("/qna/{idx}/edit")
    public String editForm(@PathVariable Long idx, @SessionAttribute(name = "user", required = false) User user, Model model) {
        if (user == null) return "redirect:/login";
        Post post = qnaService.getPost(idx);
        if (!post.getAuthorId().equals(user.getId())) return "redirect:/community/qna";
        model.addAttribute("post", post);
        return "community/qna_edit";
    }

    @PostMapping("/qna/{idx}/edit")
    public String edit(@PathVariable Long idx, @RequestParam String title, @RequestParam String content, @SessionAttribute(name = "user", required = false) User user) {
        if (user == null) return "redirect:/login";
        Post post = qnaService.getPost(idx);
        if (!post.getAuthorId().equals(user.getId())) return "redirect:/community/qna";
        qnaService.updatePost(idx, title, content);
        return "redirect:/community/qna/" + idx;
    }

    @PostMapping("/qna/{idx}/delete")
    public String delete(@PathVariable Long idx, @SessionAttribute(name = "user", required = false) User user) {
        if (user == null) return "redirect:/login";
        Post post = qnaService.getPost(idx);
        if (!post.getAuthorId().equals(user.getId())) return "redirect:/community/qna";
        qnaService.deletePost(idx);
        return "redirect:/community/qna";
    }
} 