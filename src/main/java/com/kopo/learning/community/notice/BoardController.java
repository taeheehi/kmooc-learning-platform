package com.kopo.learning.community.notice;

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
public class BoardController {

    private final NoticeService noticeService;

    @GetMapping("/notice")
    public String noticeBoard(Model model) {
        List<Post> posts = noticeService.getNoticeList();
        model.addAttribute("posts", posts);
        return "community/notice";
    }

    @GetMapping("/notice/write")
    public String writeForm(@SessionAttribute(name = "user", required = false) User user) {
        if (user == null || (!"ADMIN".equals(user.getRole()) && !"INSTRUCTOR".equals(user.getRole()))) {
            return "redirect:/community/notice";
        }
        return "community/notice_write";
    }

    @PostMapping("/notice/write")
    public String write(@RequestParam String title, 
                       @RequestParam String content,
                       @SessionAttribute(name = "user", required = false) User user) {
        if (user == null || (!"ADMIN".equals(user.getRole()) && !"INSTRUCTOR".equals(user.getRole()))) {
            return "redirect:/community/notice";
        }
        noticeService.createNotice(title, content, user);
        return "redirect:/community/notice";
    }

    @GetMapping("/notice/{idx}")
    public String view(@PathVariable Long idx, Model model) {
        Post post = noticeService.getPost(idx);
        noticeService.increaseViewCount(idx);
        model.addAttribute("post", post);
        return "community/notice_view";
    }

    @GetMapping("/notice/{idx}/edit")
    public String editForm(@PathVariable Long idx, 
                          Model model,
                          @SessionAttribute(name = "user", required = false) User user) {
        Post post = noticeService.getPost(idx);
        if (user == null || (!"ADMIN".equals(user.getRole()) && !"INSTRUCTOR".equals(user.getRole())) || !post.getAuthorId().equals(user.getId())) {
            return "redirect:/community/notice";
        }
        model.addAttribute("post", post);
        return "community/notice_edit";
    }

    @PostMapping("/notice/{idx}/edit")
    public String edit(@PathVariable Long idx,
                      @RequestParam String title,
                      @RequestParam String content,
                      @SessionAttribute(name = "user", required = false) User user) {
        Post post = noticeService.getPost(idx);
        if (user == null || (!"ADMIN".equals(user.getRole()) && !"INSTRUCTOR".equals(user.getRole())) || !post.getAuthorId().equals(user.getId())) {
            return "redirect:/community/notice";
        }
        noticeService.updateNotice(idx, title, content);
        return "redirect:/community/notice/" + idx;
    }

    @PostMapping("/notice/{idx}/delete")
    public String delete(@PathVariable Long idx,
                        @SessionAttribute(name = "user", required = false) User user) {
        Post post = noticeService.getPost(idx);
        if (user == null || (!"ADMIN".equals(user.getRole()) && !"INSTRUCTOR".equals(user.getRole())) || !post.getAuthorId().equals(user.getId())) {
            return "redirect:/community/notice";
        }
        noticeService.deleteNotice(idx);
        return "redirect:/community/notice";
    }
} 