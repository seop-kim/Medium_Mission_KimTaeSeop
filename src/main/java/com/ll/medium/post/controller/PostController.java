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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@RequestMapping("/post")
@RequiredArgsConstructor
@Slf4j
@Controller
public class PostController {
    private final PostService postService;
    private final MemberService memberService;

    /**
     * 게시글 작성
     * */
    @PostMapping("/write")
    public String write(@Valid PostForm postForm,
                        BindingResult bindingResult,
                        Principal principal,
                        boolean isPublish) {
        if (bindingResult.hasErrors()) {
            return "/post/write_form";
        }

        Member findMember = memberService.findByNickname(principal.getName());
        postService.create(postForm.getTitle(), postForm.getContent(), isPublish, findMember);
        return "redirect:/post/list";
    }

    /**
     * 전체 게시글 조회 (페이징)
     * */
    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<Post> paging = postService.getList(page);

        model.addAttribute("paging", paging);
        model.addAttribute("postName", "전체 게시글 목록"); // list.html의 게시판명
        return "/post/list";
    }

    /**
    * 나의 게시글 조회 (페이징)
    * */
    @GetMapping("/myList")
    public String myList(Principal principal,
                         Model model,
                         @RequestParam(value = "page", defaultValue = "0") int page) {
        Member findMember = memberService.findByNickname(principal.getName());
        Page<Post> paging = postService.getMyList(findMember.getId(), page);

        model.addAttribute("paging", paging);
        model.addAttribute("postName", "나의 게시글 목록"); // list.html의 게시판명
        return "/post/list";
    }

    /**
     * 다른 유저 게시글 조회 (페이징)
     * */
    @GetMapping("/{id}/userList")
    public String userList(@PathVariable("id") Long id,
                           Model model,
                           @RequestParam(value = "page", defaultValue = "0") int page) {
        String findNickname = memberService.findById(id).getNickname();
        Page<Post> paging = postService.getUserList(id, page);

        model.addAttribute("paging", paging);
        model.addAttribute("postName", findNickname + "님의 게시글 목록"); // list.html의 게시판명
        return "/post/list";
    }

    /**
     * 게시글 상세
     *
     * (조회수 처리를 쿠키로 하기 위한 req, res)
     */
    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id,
                         HttpServletRequest req,
                         HttpServletResponse res,
                         Model model) {
        Post findPost = postService.findById(id);

        // === 조회 수 처리 ===
        Cookie cookie = postService.getCookieAndViewUpdates(findPost, req.getCookies());
        res.addCookie(cookie); // 쿠키 저장

        model.addAttribute("post", findPost);
        return "/post/detail";
    }

    /**
     * 게시글 작성 폼 이동
     * */
    @GetMapping("/write")
    public String writeForm(PostForm postForm) {
        return "/post/write_form";
    }

    /**
     * 게시글 수정 폼 이동
     * */
    @GetMapping("/{id}/modify")
    public String modifyForm(@PathVariable("id") Long id,
                             PostForm postForm,
                             Principal principal) {

        Post findPost = postService.findById(id);

        if (!principal.getName().equals(findPost.getAuthor().getNickname())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        postForm.setTitle(findPost.getTitle());
        postForm.setContent(findPost.getContent());
        postForm.setPublish(findPost.isPublish());

        return "/post/modify_form";
    }

    /**
     * 게시글 수정
     * */
    @PostMapping("/{id}/modify")
    public String modify(@PathVariable("id") Long id,
                         PostForm postForm,
                         boolean isPublish,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "/post/modify_form";
        }

        Post findPost = postService.findById(id);
        postService.editPost(findPost, postForm.getTitle(), postForm.getContent(), isPublish);

        return "redirect:/post/%d".formatted(id);
    }

    /**
     * 게시글 삭제
     * */
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id,
                         Principal principal) {
        Post findOne = postService.findById(id);

        if (!principal.getName().equals(findOne.getAuthor().getNickname())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한 없음");
        }

        postService.deletePost(findOne);
        return "redirect:/post/list";
    }

    /**
     * 게시글 검색
     *  /
     *  (제목)
     * */
    @GetMapping("/search")
    public String keywordList(Model model,
                              String keyword,
                              @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<Post> paging = postService.getKeywordList(keyword, page);

        model.addAttribute("paging", paging);
        model.addAttribute("keyword", keyword);
        return "/post/list";
    }

    /**
     * 게시글 추천
     * */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/like/{id}")
    public String postLike(Principal principal, @PathVariable("id") Long id) {
        Post findPost = postService.findById(id);
        Member findMember = memberService.findByNickname(principal.getName());

        postService.updateLike(findPost, findMember);
        return "redirect:/post/%d".formatted(id);
    }
}
