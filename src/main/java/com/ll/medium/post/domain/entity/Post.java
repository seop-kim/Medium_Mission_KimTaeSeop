package com.ll.medium.post.domain.entity;

import com.ll.medium.global.jpa.BaseEntity;
import com.ll.medium.member.member.entity.Member;
import com.ll.medium.post.postComment.entity.PostComment;
import com.ll.medium.post.postLike.entity.PostLike;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Setter
public class Post extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Member author;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String body;
    private boolean isPublished;

    @Setter(AccessLevel.PROTECTED)
    private long hit ;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PostLike> likes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @OrderBy("id DESC")
    private List<PostComment> comments = new ArrayList<>();

    public void increaseHit() {
        hit++;
    }

    public void addLike(Member member) {
        if (hasLike(member)) {
            return;
        }

        likes.add(PostLike.builder()
                .post(this)
                .member(member)
                .build());
    }

    public boolean hasLike(Member member) {
        return likes.stream()
                .anyMatch(postLike -> postLike.getMember().equals(member));
    }

    public void deleteLike(Member member) {
        likes.removeIf(postLike -> postLike.getMember().equals(member));
    }

    public PostComment writeComment(Member actor, String body) {
        PostComment postComment = PostComment.builder()
                .post(this)
                .author(actor)
                .body(body)
                .build();

        comments.add(postComment);

        return postComment;
    }
}