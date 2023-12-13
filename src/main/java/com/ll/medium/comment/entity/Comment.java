package com.ll.medium.comment.entity;

import com.ll.medium.member.entity.Member;
import com.ll.medium.post.entity.Post;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member author;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime regiDate;

    @ManyToOne
    private Post post;

    @Builder
    public Comment(String content, Member author, Post post) {
        this.content = content;
        this.author = author;
        this.post = post;
        this.regiDate = LocalDateTime.now();
    }

    public void editContent(String content) {
        this.content = content;
    }
}
