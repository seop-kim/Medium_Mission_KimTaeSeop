package com.ll.medium.util.init;

import com.ll.medium.member.entity.Member;
import com.ll.medium.member.role.MemberRole;
import com.ll.medium.member.service.MemberService;
import com.ll.medium.post.service.PostService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!prod")
@Configuration
public class NotProd {

    @Bean
    public ApplicationRunner initNotProdData(MemberService memberService, PostService postService) {
        return args -> {

            if (memberService.count() == 0) {
                Member findOne = memberService.join("admin", "관리자", "1234");
                findOne.roleUpdate(MemberRole.ADMIN);

                memberService.join("member", "김태섭", "1234");
            }

            if (postService.count() == 0) {
                for (int i = 1; i <= 100; i++) {
                    postService.create("test title " + i, "test content " + i,true, memberService.findById(1L));
                }

                postService.create("member test title ", "member test content ",false, memberService.findById(2L));
            }

        };
    }
}
