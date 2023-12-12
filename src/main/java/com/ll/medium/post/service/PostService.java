package com.ll.medium.post.service;

import com.ll.medium.member.entity.Member;
import com.ll.medium.post.entity.Post;
import com.ll.medium.post.repository.PostRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    // == 게시글 목록 ==
    public Page<Post> getList(int page) {
        List<Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("regiDate")); // reverse
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return postRepository.findAll(pageable);
    }

    public Page<Post> getUserList(Long id, int page){
        List<Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("regiDate")); // reverse
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return postRepository.findByAuthor_Id(id, pageable);
    }

    // == 게시글 작성 ==
    @Transactional
    public void create(String title, String content, Member member) {
        Post post = Post.builder()
                .title(title)
                .content(content)
                .author(member)
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

    // == 게시글 수정 ==
    @Transactional
    public void modify(Post post, String title, String content) {
        post.edit(title, content);
    }
}