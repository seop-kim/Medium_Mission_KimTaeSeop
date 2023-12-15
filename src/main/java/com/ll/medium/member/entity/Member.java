package com.ll.medium.member.entity;

import com.ll.medium.member.role.MemberRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    @Builder
    public Member(String nickName, String username, String password) {
        this.nickname = nickName;
        this.username = username;
        this.password = password;
        this.memberRole = MemberRole.MEMBER;
    }

    public void roleUpdate(MemberRole memberRole) {
        this.memberRole = memberRole;
    }
}
