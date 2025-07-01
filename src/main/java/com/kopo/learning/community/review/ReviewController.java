package com.kopo.learning.community.review;

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
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/review")
    public String reviewBoard(Model model, @RequestParam(defaultValue = "1") int page) {
        final int PAGE_SIZE = 20;
        int total = reviewService.countReviewPosts();
        int totalPages = (int) Math.ceil((double) total / PAGE_SIZE);
        List<Post> posts = reviewService.getReviewList(page, PAGE_SIZE);
        model.addAttribute("posts", posts);
        model.addAttribute("page", page);
        model.addAttribute("totalPages", totalPages);
        return "community/review";
    }

    @GetMapping("/review/write")
    public String writeForm() {
        return "community/review_write";
    }

    @PostMapping("/review/write")
    public String write(@RequestParam String title, @RequestParam String content, @SessionAttribute(name = "user", required = false) User user) {
        if (user == null) return "redirect:/login";
        reviewService.createPost(title, content, user);
        return "redirect:/community/review";
    }

    @GetMapping("/review/{idx}")
    public String viewPost(@PathVariable Long idx, Model model) {
        Post post = reviewService.getPost(idx);
        reviewService.increaseViewCount(idx);
        model.addAttribute("post", post);
        return "community/review_view";
    }

    @GetMapping("/review/{idx}/edit")
    public String editForm(@PathVariable Long idx, @SessionAttribute(name = "user", required = false) User user, Model model) {
        if (user == null) return "redirect:/login";
        Post post = reviewService.getPost(idx);
        if (!post.getAuthorId().equals(user.getId())) return "redirect:/community/review";
        model.addAttribute("post", post);
        return "community/review_edit";
    }

    @PostMapping("/review/{idx}/edit")
    public String edit(@PathVariable Long idx, @RequestParam String title, @RequestParam String content, @SessionAttribute(name = "user", required = false) User user) {
        if (user == null) return "redirect:/login";
        Post post = reviewService.getPost(idx);
        if (!post.getAuthorId().equals(user.getId())) return "redirect:/community/review";
        reviewService.updatePost(idx, title, content);
        return "redirect:/community/review/" + idx;
    }

    @PostMapping("/review/{idx}/delete")
    public String delete(@PathVariable Long idx, @SessionAttribute(name = "user", required = false) User user) {
        if (user == null) return "redirect:/login";
        Post post = reviewService.getPost(idx);
        if (!post.getAuthorId().equals(user.getId())) return "redirect:/community/review";
        reviewService.deletePost(idx);
        return "redirect:/community/review";
    }
} 