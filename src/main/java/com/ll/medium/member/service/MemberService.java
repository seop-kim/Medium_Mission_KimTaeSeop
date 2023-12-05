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
    public Member join(String nickName, String userName, String password) {
        Member member = Member.builder()
                .nickName(nickName)
                .username(userName)
                .password(passwordEncoder.encode(password))
                .build();

        memberRepository.save(member);
        return member;
    }


    // == id 조회 ==

    // == nickName 조회 ==
    public Member findByNickName(String nickName) {
        Optional<Member> findOne = memberRepository.findByNickName(nickName);

        if (findOne.isEmpty()) {
            throw new IllegalArgumentException("검색하는 닉네임의 사용자가 없습니다.");
        }

        return findOne.get();
    }
}
