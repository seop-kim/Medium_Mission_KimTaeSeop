package com.ll.medium.post.service;

import com.ll.medium.member.entity.Member;
import com.ll.medium.post.entity.Post;
import com.ll.medium.post.repository.PostRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;


    // == 게시글 목록 ==
    public Page<Post> getList(int page) {
        List<Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("regiDate")); // reverse
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return postRepository.findAllByIsPublishTrue(pageable);
    }


    // == 특정 사용자 게시글 목록 ==
    public Page<Post> getUserList(Long id, int page) {
        List<Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("regiDate")); // reverse
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return postRepository.findByAuthor_IdAndIsPublishTrue(id, pageable);
    }

    // == 나의 게시글 목록 == * 비공개 포스트 확인 가능
    public Page<Post> getMyList(Long id, int page) {
        List<Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("regiDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return postRepository.findByAuthor_Id(id, pageable);
    }

    // == 게시글 작성 ==
    @Transactional
    public void create(String title, String content, boolean isPublish, Member member) {
        Post post = Post.builder()
                .title(title)
                .content(content)
                .author(member)
                .isPublish(isPublish)
                .build();

        postRepository.save(post);
    }

    // == 게시글 조회 ==
    public Post getPost(Long id) {
        return postRepository.findById(id).get();
    }

    // == 게시글 삭제 ==
    @Transactional
    public void removePost(Post post) {
        postRepository.delete(post);
    }

    // == 게시글 수정 ==

    @Transactional
    public void modify(Post post, String title, String content, boolean isPublish) {
        post.edit(title, content, isPublish);
    }
    // == 게시물 검색 ==

    public Page<Post> getKeywordList(@DefaultValue(value = "") String keyword, int page) {
        log.info("keyword : " + keyword);
        List<Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("regiDate")); // reverse
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));

        return postRepository.findByTitleContainingAndIsPublishTrue(keyword, pageable);
    }

    public Post findById(Long id) {
        Optional<Post> findOndOp = postRepository.findById(id);

        if (findOndOp.isEmpty()) {
            throw new IllegalArgumentException("검색된 포스트가 없습니다.");
        }

        return findOndOp.get();
    }


    public long count() {
        return postRepository.count();
    }
}
