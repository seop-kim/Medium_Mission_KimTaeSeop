package com.ll.medium.post.service;

import com.ll.medium.member.entity.Member;
import com.ll.medium.post.entity.Post;
import com.ll.medium.post.repository.PostRepository;
import jakarta.servlet.http.Cookie;
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

    /**
     * 메인페이지에 사용되는 30개 게시물 조회 기능
     * */
    public List<Post> getHomeList() {
        return postRepository.findTop30ByIsPublishTrueOrderByRegiDateDesc();
    }

    /**
     * 게시글 작성 기능
     * */
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

    /**
     * 전체 게시글 조회 기능
     * */
    public Page<Post> getList(int page) {
        Pageable pageable = makePageable(page);
        return postRepository.findAllByIsPublishTrue(pageable);
    }

    /**
     * 나의 게시글 조회 기능 (비공개 게시물 조회)
     * */
    public Page<Post> getMyList(Long id, int page) {
        Pageable pageable = makePageable(page);
        return postRepository.findByAuthor_Id(id, pageable);
    }

    /**
     * 특정 사용자 게시글 조회 기능
     * */
    public Page<Post> getUserList(Long id, int page) {
        Pageable pageable = makePageable(page);
        return postRepository.findByAuthor_IdAndIsPublishTrue(id, pageable);
    }

    /**
     * ID를 통한 게시글 조회 기능
     * */
    @Transactional
    public Post findById(Long id) {
        Optional<Post> findOndOp = postRepository.findById(id);

        if (findOndOp.isEmpty()) {
            throw new IllegalArgumentException("검색된 포스트가 없습니다.");
        }

        return findOndOp.get();
    }

    /**
     *게시글 삭제 기능
     * */
    @Transactional
    public void deletePost(Post post) {
        postRepository.delete(post);
    }

    /**
     *게시글 수정 기능
     * */
    @Transactional
    public void editPost(Post post, String title, String content, boolean isPublish) {
        post.edit(title, content, isPublish);
    }

    /**
     * 게시글 검색 기능 (제목)
     * */
    public Page<Post> getKeywordList(@DefaultValue(value = "") String keyword, int page) {
        log.info("keyword : " + keyword);
        Pageable pageable = makePageable(page);
        return postRepository.findByTitleContainingAndIsPublishTrue(keyword, pageable);
    }

    /**
     * 게시글 추천 기능
     *
     *
     * */
    @Transactional
    public void updateLike(Post post, Member member) {
        // 이미 추천을 눌렀으면 추천 취소
        if (post.getLike().contains(member)) {
            post.getLike().remove(member);
            return;
        }

        post.getLike().add(member);
    }

    /**
     *게시글 조회수 처리 기능
     * */
    @Transactional
    public Cookie getCookieAndViewUpdates(Post post, Cookie[] cookies) {
        String postName = "[" + post.getId() + "]";
        Cookie resultCookie = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("postView")) {
                    resultCookie = cookie;
                }
            }
        }

        if (resultCookie != null) {
            if (!resultCookie.getValue().contains(postName)) {
                post.increaseView();
                resultCookie.setValue(resultCookie.getValue() + "_" + postName);
                resultCookie.setPath("/");
                resultCookie.setMaxAge(60); // 1분 설정
            }
        } else {
            post.increaseView();
            resultCookie = new Cookie("postView", postName);
            resultCookie.setPath("/");
            resultCookie.setMaxAge(60); // 1분 설정
        }

        return resultCookie;
    }

    /**
     * 전체 게시글 수 조회 기능
     * @return posts.size()
     */
    public long count() {
        return postRepository.count();
    }

    /**
     * pageable 객체 생성
     * */
    private Pageable makePageable(int page) {
        List<Order> sorts = new ArrayList<>();
        sorts.add(Order.desc("regiDate"));
        return PageRequest.of(page, 10, Sort.by(sorts));
    }
}
