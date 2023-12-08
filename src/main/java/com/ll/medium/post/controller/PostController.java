package com.ll.medium.post.controller;

import com.ll.medium.member.entity.Member;
import com.ll.medium.member.service.MemberService;
import com.ll.medium.post.entity.Post;
import com.ll.medium.post.service.PostService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

@RequestMapping("/post")
@RequiredArgsConstructor
@Controller
public class PostController {
    private final PostService postService;
    private final MemberService memberService;

    @GetMapping("/home")
    @ResponseBody
    public String home() {
        return "웹 홈";
    }

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<Post> paging = postService.getList(page);
        model.addAttribute("paging", paging);
        return "/post/list";
    }

    @GetMapping("/myList")
    @ResponseBody
    public String myList() {
        return "내 글 리스트 페이지입니다.";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id,
                         Model model) {
        Post post = postService.getPost(id);
        model.addAttribute("post", post);
        return "/post/detail";
    }

    @GetMapping("/write")
    public String writeForm(Post post) {
        return "/post/write_form";
    }

    @PostMapping("/write")
    public String write(Post post, Principal principal) {
        Member findMember = memberService.findByNickname(principal.getName());
        postService.create(post.getTitle(), post.getContent(), findMember);
        return "redirect:/post/list";
    }

    @GetMapping("/{id}/modify")
    @ResponseBody
    public String modifyForm(@PathVariable("id") Long id) {
        return "게시글 수정 폼 입니다.";
    }

    @PutMapping("/{id}/modify")
    @ResponseBody
    public String modify(@PathVariable("id") Long id) {
        return "redirect:/게시글 수정 기능입니다.";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id,
                         Principal principal) {
        Post findOne = postService.findById(id);

        if (!principal.getName().equals(findOne.getAuthor().getNickname())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한 없음");
        }

        postService.removePost(findOne);
        return "redirect:/post/list";
    }
}
