package com.ll.medium.post.postComment.entity;

import com.ll.medium.global.jpa.BaseEntity;
import com.ll.medium.member.member.entity.Member;
import com.ll.medium.post.domain.entity.Post;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Setter
public class PostComment extends BaseEntity {
    @ManyToOne
    private Member author;
    @ManyToOne
    private Post post;
    @Column(columnDefinition = "TEXT")
    private String body;
}