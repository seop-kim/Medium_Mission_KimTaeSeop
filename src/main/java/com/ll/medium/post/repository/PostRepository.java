package com.ll.medium.post.repository;

import com.ll.medium.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAll(Pageable pageable);

    Page<Post> findAllByIsPublishTrue(Pageable pageable);

    Page<Post> findByAuthor_IdAndIsPublishTrue(Long id, Pageable pageable);

    Page<Post> findByTitleContainingAndIsPublishTrue(String keyword, Pageable pageable);

    Page<Post> findByAuthor_Id(Long id, Pageable pageable);
}
