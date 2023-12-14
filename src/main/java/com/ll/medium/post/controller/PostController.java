package com.ll.medium.post.controller;

import com.ll.medium.member.entity.Member;
import com.ll.medium.member.service.MemberService;
import com.ll.medium.post.entity.Post;
import com.ll.medium.post.service.PostService;
import com.ll.medium.post.util.PostForm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    // == 게시글 목록 ==
    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(value = "page",
                               defaultValue = "0") int page) {
        Page<Post> paging = postService.getList(page);
        model.addAttribute("paging", paging);
        model.addAttribute("postName", "전체 게시글 목록");
        return "/post/list";
    }

    // == 나의 게시글 목록 ==
    @GetMapping("/myList")
    public String myList(Principal principal,
                         Model model,
                         @RequestParam(value = "page", defaultValue = "0") int page) {
        Member findOne = memberService.findByNickname(principal.getName());

        Page<Post> paging = postService.getMyList(findOne.getId(), page);
        model.addAttribute("paging", paging);
        model.addAttribute("postName", "나의 게시글 목록");
        return "/post/list";
    }

    // == 다른 유저 게시글 목록 ==
    @GetMapping("/{id}/userList")
    public String userList(@PathVariable("id") Long id,
                           Model model,
                           @RequestParam(value = "page", defaultValue = "0") int page) {

        String findNickname = memberService.findById(id).getNickname();

        Page<Post> paging = postService.getUserList(id, page);
        model.addAttribute("paging", paging);
        model.addAttribute("postName", findNickname + "님의 게시글 목록");
        return "/post/list";
    }

    // == 게시글 상세 보기 ==
    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id,
                         HttpServletRequest req,
                         HttpServletResponse res,
                         Model model) {
        viewUpdate(id, req, res);
        Post post = postService.getPost(id);
        model.addAttribute("post", post);
        return "/post/detail";
    }

    private void viewUpdate(Long id, HttpServletRequest req, HttpServletResponse res) {
        Cookie oldCookie = null;

        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("postView")) {
                    oldCookie = cookie;
                }
            }
        }

        if (oldCookie != null) {
            if (!oldCookie.getValue().contains("[" + id.toString() + "]")) {
                postService.viewUp(id);
                oldCookie.setValue(oldCookie.getValue() + "_[" + id + "]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60); // 1분 설정
                res.addCookie(oldCookie);
            }
        } else {
            postService.viewUp(id);
            Cookie newCookie = new Cookie("postView", "[" + id + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60); // 1분 설정
            res.addCookie(newCookie);
        }
    }


    // == 게시글 작성 폼 ==
    @GetMapping("/write")
    public String writeForm(PostForm postForm) {
        return "/post/write_form";
    }

    // == 게시글 작성 ==
    @PostMapping("/write")
    public String write(@Valid PostForm postForm,
                        Principal principal,
                        boolean isPublish,
                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/post/write_form";
        }

        Member findMember = memberService.findByNickname(principal.getName());
        postService.create(postForm.getTitle(), postForm.getContent(), isPublish, findMember);
        return "redirect:/post/list";
    }

    // == 게시글 수정 폼 ==
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
        postForm.setPublish(findOne.isPublish());

        return "/post/modify_form";
    }

    // == 게시글 수정 ==
    @PostMapping("/{id}/modify")
    public String modify(@PathVariable("id") Long id,
                         PostForm postForm,
                         boolean isPublish,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "/post/modify_form";
        }

        Post findOne = postService.findById(id);
        postService.modify(findOne, postForm.getTitle(), postForm.getContent(), isPublish);

        return "redirect:/post/%d".formatted(id);
    }

    // == 게시글 삭제 ==
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

    // == 게시글 검색 ==
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
