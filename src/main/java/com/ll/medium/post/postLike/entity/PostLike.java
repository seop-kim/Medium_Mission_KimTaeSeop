package com.ll.medium.post.postLike.entity;

import com.ll.medium.global.jpa.IdEntity;
import com.ll.medium.member.member.entity.Member;
import com.ll.medium.post.domain.entity.Post;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PostLike extends IdEntity {
    @ManyToOne
    private Post post;
    @ManyToOne
    private Member member;
}
