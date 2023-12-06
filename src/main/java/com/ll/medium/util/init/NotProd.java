package com.ll.medium.util.init;

import com.ll.medium.member.entity.Member;
import com.ll.medium.member.role.MemberRole;
import com.ll.medium.member.service.MemberService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!prod")
@Configuration
public class NotProd {

    @Bean
    public ApplicationRunner initNotProdData(MemberService memberService) {
        return args -> {
            if (memberService.count() == 0) {
                Member findOne = memberService.join("admin", "관리자", "1234");
                findOne.roleUpdate(MemberRole.ADMIN);
            }
        };
    }
}
