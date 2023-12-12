package com.ll.medium.post.controller;

import com.ll.medium.member.entity.Member;
import com.ll.medium.member.service.MemberService;
import com.ll.medium.post.entity.Post;
import com.ll.medium.post.service.PostService;
import com.ll.medium.post.util.PostForm;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
@Slf4j
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
    public String list(Model model,
                       @RequestParam(value = "page",
                               defaultValue = "0") int page) {
        Page<Post> paging = postService.getList(page);
        model.addAttribute("paging", paging);
        return "/post/list";
    }

    @GetMapping("/myList")
    public String myList(Principal principal) {
        Member findOne = memberService.findByNickname(principal.getName());
        log.info("member id : " + findOne.getId());
        return "redirect:/post/%d/userList".formatted(findOne.getId());

    }

    @GetMapping("/{id}/userList")
    public String userList(@PathVariable("id") Long id,
                           Model model,
                           @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<Post> paging = postService.getUserList(id, page);
        model.addAttribute("paging", paging);
        return "/post/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id,
                         Model model) {
        Post post = postService.getPost(id);
        model.addAttribute("post", post);
        return "/post/detail";
    }

    @GetMapping("/write")
    public String writeForm(PostForm postForm) {
        return "/post/write_form";
    }

    @PostMapping("/write")
    public String write(@Valid PostForm postForm,
                        Principal principal,
                        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "/post/write_form";
        }

        Member findMember = memberService.findByNickname(principal.getName());
        postService.create(postForm.getTitle(), postForm.getContent(), findMember);
        return "redirect:/post/list";
    }

    @GetMapping("/{id}/modify")
    public String modifyForm(@PathVariable("id") Long id,
                             PostForm postForm,
                             Principal principal) {

        Post findOne = postService.findById(id);

        if (!principal.getName().equals(findOne.getAuthor().getNickname())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        postForm.setTitle(findOne.getTitle());
        postForm.setContent(findOne.getContent());

        return "/post/modify_form";
    }

    @PostMapping("/{id}/modify")
    public String modify(@PathVariable("id") Long id,
                         PostForm postForm,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "/post/modify_form";
        }

        Post findOne = postService.findById(id);
        log.info("update title : " + postForm.getTitle() + " | update content : " + postForm.getContent());
        postService.modify(findOne, postForm.getTitle(), postForm.getContent());

        return "redirect:/post/%d".formatted(id);
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

    @GetMapping("/search")
    public String keywordList(Model model,
                              String keyword,
                              @RequestParam(value = "page", defaultValue = "0") int page) {

        Page<Post> paging = postService.getKeywordList(keyword, page);
        model.addAttribute("paging", paging);
        model.addAttribute("keyword", keyword);
        return "/post/list";
    }
}
