package com.ll.medium.member.service;

import com.ll.medium.member.entity.Member;
import com.ll.medium.member.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // == 회원가입 ==
    public Member join(String nickname, String username, String password) {
        Member member = Member.builder()
                .nickName(nickname)
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();

        memberRepository.save(member);
        return member;
    }


    // == id 조회 ==
    public Member findById(Long id) {
        Optional<Member> findOne = memberRepository.findById(id);

        if (findOne.isEmpty()) {
            throw new IllegalArgumentException("검색하는 id의 사용자가 없습니다.");
        }

        return findOne.get();
    }

    // == nickName 조회 ==
    public Member findByNickname(String nickname) {
        System.out.println("nickName : " + nickname);
        Optional<Member> findOne = memberRepository.findByNickname(nickname);
        System.out.println("nickName2 : "  + findOne.get().getNickname());

        if (findOne.isEmpty()) {
            throw new IllegalArgumentException("검색하는 닉네임의 사용자가 없습니다.");
        }

        return findOne.get();
    }

    // == member count ==
    public long count() {
        return memberRepository.count();
    }
}
