package com.ll.medium.post.post.service;

import com.ll.medium.member.member.entity.Member;
import com.ll.medium.post.domain.entity.Post;
import com.ll.medium.post.post.repository.PostRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;

    @Transactional
    public Post write(Member author, String title, String body, boolean isPublished) {
        Post post = Post.builder()
                .author(author)
                .title(title)
                .body(body)
                .isPublished(isPublished)
                .build();

        return postRepository.save(post);
    }

    public Object findTop30ByIsPublishedOrderByIdDesc(boolean isPublished) {
        return postRepository.findTop30ByIsPublishedOrderByIdDesc(isPublished);
    }

    public Optional<Post> findById(long id) {
        return postRepository.findById(id);
    }

    public Page<Post> search(String kw, Pageable pageable) {
        return postRepository.search(true, kw, pageable);
    }

    public Page<Post> search(Member author, Boolean isPublished, String kw, Pageable pageable) {
        return postRepository.search(author, isPublished, kw, pageable);
    }

    public boolean canModify(Member actor, Post post) {
        return actor.equals(post.getAuthor());
    }

    @Transactional
    public void modify(Post post, String title, String body, boolean published) {
        post.setTitle(title);
        post.setBody(body);
        post.setPublished(published);
    }

    public boolean canDelete(Member actor, Post post) {
        if (actor.isAdmin()) {
            return true;
        }

        return actor.equals(post.getAuthor());
    }

    @Transactional
    public void delete(Post post) {
        postRepository.delete(post);
    }

    @Transactional
    public void increaseHit(Post post) {
        post.increaseHit();
    }
}