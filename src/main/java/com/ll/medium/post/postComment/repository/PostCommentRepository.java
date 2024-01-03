package com.ll.medium.post.postComment.repository;

import com.ll.medium.post.postComment.entity.PostComment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    Optional<PostComment> findCommentById(long id);
}