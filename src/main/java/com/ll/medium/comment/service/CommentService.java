package com.ll.medium.comment.service;

import com.ll.medium.comment.entity.Comment;
import com.ll.medium.comment.repository.CommentRepository;
import com.ll.medium.member.entity.Member;
import com.ll.medium.post.entity.Post;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;

    /**
     * 댓글 작성 기능
     * */
    public void write(String content, Member member, Post post) {
        Comment comment = Comment.builder()
                .content(content)
                .author(member)
                .post(post)
                .build();

        commentRepository.save(comment);
    }

    /**
     * 댓글 수정 기능
     * */
    @Transactional
    public void editComment(Comment comment, String content) {
        comment.editContent(content);
    }

    /**
     *댓글 삭제 기능
     * */
    @Transactional
    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }

    /**
     * ID로 댓글 조회
     * */
    public Comment findById(Long id) {
        Optional<Comment> findComment = commentRepository.findById(id);

        if (findComment.isEmpty()) {
            log.error("[CommentServiceException] answer not found");
            throw new IllegalArgumentException("[CommentServiceException] answer not found");
        }

        return findComment.get();
    }
}
