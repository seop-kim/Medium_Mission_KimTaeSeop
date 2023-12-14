package com.ll.medium.member.service;

import com.ll.medium.member.entity.Member;
import com.ll.medium.member.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     *회원가입 기능
     * */
    @Transactional
    public Member join(String nickname, String username, String password) {
        Member member = Member.builder()
                .nickName(nickname)
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();

        memberRepository.save(member);
        return member;
    }


    /**
     * ID로 회원 조회
     * */
    public Member findById(Long id) {
        Optional<Member> findMember = memberRepository.findById(id);

        if (findMember.isEmpty()) {
            throw new IllegalArgumentException("IDd의 회원이 없습니다.");
        }

        return findMember.get();
    }

    /**
     * NickName으로 회원 조회
     * */
    public Member findByNickname(String nickname) {
        Optional<Member> findMember = memberRepository.findByNickname(nickname);

        if (findMember.isEmpty()) {
            throw new IllegalArgumentException("검색하는 닉네임의 회원이 없습니다.");
        }

        return findMember.get();
    }

    /**
     * 전체 회원 수 조회
     * @return members.size()
     */
    public long count() {
        return memberRepository.count();
    }
}
