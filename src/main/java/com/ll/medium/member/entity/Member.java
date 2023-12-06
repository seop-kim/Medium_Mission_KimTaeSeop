package com.ll.medium.member.entity;

import com.ll.medium.member.role.MemberRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nickname;

    private String username;

    private String password;

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
