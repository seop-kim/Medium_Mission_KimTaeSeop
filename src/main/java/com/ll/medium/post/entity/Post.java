package com.ll.medium.post.entity;

import com.ll.medium.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member author;

    private String title;

    private String content;

    private LocalDateTime regiDate;

    private boolean isPublish;

    @Builder
    public Post(String title, String content, Member author, boolean isPublish) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.regiDate = LocalDateTime.now();
        this.isPublish = isPublish;
    }

    public void edit(String title, String content, boolean isPublish) {
        this.title = title;
        this.content = content;
        this.isPublish = isPublish;
    }
}
