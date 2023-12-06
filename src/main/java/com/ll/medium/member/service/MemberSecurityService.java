package com.ll.medium.member.service;

import com.ll.medium.member.entity.Member;
import com.ll.medium.member.role.MemberRole;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberSecurityService implements UserDetailsService {
    private final MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        Member findOne = memberService.findByNickname(nickname);

        List<GrantedAuthority> authorities = new ArrayList<>();

        if (findOne.getMemberRole() == MemberRole.ADMIN) { // admin 경우만 분기 처리
            authorities.add(new SimpleGrantedAuthority(MemberRole.ADMIN.getRole()));
            return new User(findOne.getNickname(), findOne.getPassword(), authorities);
        }

        authorities.add(new SimpleGrantedAuthority(MemberRole.MEMBER.getRole()));
        return new User(findOne.getNickname(), findOne.getPassword(), authorities);
    }
}
