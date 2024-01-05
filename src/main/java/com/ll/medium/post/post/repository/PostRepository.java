package com.ll.medium.post.post.repository;

import com.ll.medium.member.member.entity.Member;
import com.ll.medium.post.domain.entity.Post;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    List<Post> findTop30ByIsPublishedOrderByIdDesc(boolean isPublished);

    Page<Post> findByTitleContainingIgnoreCaseOrBodyContainingIgnoreCase(String kw, String kw_, Pageable pageable);

    Page<Post> findByAuthorAndTitleContainingIgnoreCaseOrAuthorAndBodyContainingIgnoreCase(Member author, String kw, Member author_, String kw_, Pageable pageable);
}