package com.ll.medium.comment.controller;

import com.ll.medium.comment.entity.Comment;
import com.ll.medium.comment.service.CommentService;
import com.ll.medium.comment.util.CommentForm;
import com.ll.medium.member.entity.Member;
import com.ll.medium.member.service.MemberService;
import com.ll.medium.post.entity.Post;
import com.ll.medium.post.service.PostService;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/comment")
@Controller
public class CommentController {
    private final PostService postService;
    private final MemberService memberService;
    private final CommentService commentService;

    /**
     * 댓글 작성
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write/{id}")
    public String writeComment(@PathVariable("id") Long id,
                               @Valid CommentForm commentForm,
                               BindingResult bindingResult,
                               Model model,
                               Principal principal) {
        Post post = postService.findById(id);
        Member findMember = memberService.findByNickname(principal.getName());

        if (bindingResult.hasErrors()) {
            model.addAttribute("post", post);
            return "/post/detail";
        }

        commentService.write(commentForm.getContent(), findMember, post);
        return "redirect:/post/%d".formatted(id);
    }

    /**
     * 댓글 수정
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/edit/{id}")
    public ResponseEntity<String> editComment(@PathVariable Long id, @RequestBody CommentUpdateRequest request) {
        Comment findComment = commentService.findById(id);
        commentService.editComment(findComment, request.getContent());
        return ResponseEntity.ok("댓글이 업데이트되었습니다.");
    }

    /**
     * 댓글 삭제
     */
    @GetMapping("/{commentId}/delete")
    public String deleteComment(@PathVariable Long commentId) {
        Comment findComment = commentService.findById(commentId);
        Long postId = findComment.getPost().getId();

        commentService.deleteComment(findComment);
        return "redirect:/post/%d".formatted(postId);

    }
}

@Getter
@Setter
class CommentUpdateRequest {
    private String content;
}
