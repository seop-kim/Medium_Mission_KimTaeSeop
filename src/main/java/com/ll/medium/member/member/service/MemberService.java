package com.ll.medium.member.member.service;

import com.ll.medium.global.rsData.RsData;
import com.ll.medium.member.member.entity.Member;
import com.ll.medium.member.member.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RsData<Member> join(String username, String password) {
        if (findByUsername(username).isPresent()) {
            return RsData.of("400-2", "이미 존재하는 회원입니다.");
        }

        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();
        memberRepository.save(member);

        return RsData.of("200", "%s님 환영합니다. 회원가입이 완료되었습니다. 로그인 후 이용해주세요.".formatted(member.getUsername()), member);
    }

    @Transactional
    public void membershipUpdate(long id, boolean membership) {
        Member member = memberRepository.findById(id).get();
        member.setPaid(membership);
    }

    public Optional<Member> findById(long id) {
        return memberRepository.findById(id);
    }

    @Transactional
    public void updateMembership(Member member, boolean membership) {
        member.setPaid(membership);
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public long count() {
        return memberRepository.count();
    }
}