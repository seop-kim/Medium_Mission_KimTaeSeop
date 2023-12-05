package com.ll.medium.member.repository;

import com.ll.medium.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUserName(String userName);
    Optional<Member> findByNickName(String nickName);

}
