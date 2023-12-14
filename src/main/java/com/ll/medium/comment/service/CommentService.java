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

    public void write(String content, Member member, Post post) {
        Comment comment = Comment.builder()
                .content(content)
                .author(member)
                .post(post)
                .build();
        commentRepository.save(comment);
    }

    public Comment findById(Long id) {
        Optional<Comment> findOne = commentRepository.findById(id);

        if (findOne.isEmpty()) {
            log.error("[CommentServiceException] answer not found");
            throw new IllegalArgumentException("[CommentServiceException] answer not found");
        }

        return findOne.get();
    }

    @Transactional
    public void editComment(Long id, String content) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            comment.editContent(content);
        } else {
            // 댓글이 존재하지 않을 경우 예외 처리 또는 적절한 로직 수행
            throw new IllegalArgumentException("댓글이 존재하지 않습니다. id=" + id);
        }
    }

    @Transactional
    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }
}
