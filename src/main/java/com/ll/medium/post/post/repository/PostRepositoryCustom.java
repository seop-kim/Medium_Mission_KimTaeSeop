package com.ll.medium.post.post.repository;

import com.ll.medium.member.member.entity.Member;
import com.ll.medium.post.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<Post> search(boolean isPublished, String kw, Pageable pageable);

    Page<Post> search(Member author, Boolean isPublished, String kw, Pageable pageable);
}
