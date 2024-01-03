package com.ll.medium.post.domain.entity;

import com.ll.medium.global.jpa.BaseEntity;
import com.ll.medium.member.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
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
    private String body;
    private boolean isPublished;
    @Setter(AccessLevel.PROTECTED)
    private long hit ;

    public void increaseHit() {
        hit++;
    }
}