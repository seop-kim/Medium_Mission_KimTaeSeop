package com.ll.medium.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.ll.medium.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class MemberServiceTest {
    @Autowired
    private MemberService memberService;


    @DisplayName("[회원 등록] 회원가입이 완료되고 회원을 조회하면 id 값이 있고 등록된 데이터가 동일하다.")
    @Transactional
    @Test
    void t1() {
        String regiUserName = "김태섭";
        String regiNickName = "체이";
        String regiPass = "1234";

        Member joinMember = memberService.join(regiNickName, regiUserName, regiPass);
        Member findOne = memberService.findByNickName(regiNickName);

        assertAll(
                ()->assertThat(findOne.getId()).isNotNull(),
                () -> assertThat(findOne.getNickName()).isEqualTo(joinMember.getNickName()),
                () -> assertThat(findOne.getUserName()).isEqualTo(joinMember.getUserName())
        );
    }

}