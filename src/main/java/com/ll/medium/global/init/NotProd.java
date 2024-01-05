package com.ll.medium.global.init;

import ch.qos.logback.core.joran.conditional.IfAction;
import com.ll.medium.member.member.entity.Member;
import com.ll.medium.member.member.service.MemberService;
import com.ll.medium.post.domain.entity.Post;
import com.ll.medium.post.post.service.PostService;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@Profile("!prod")
@Slf4j
@RequiredArgsConstructor
public class NotProd {
    @Autowired
    @Lazy
    private NotProd self;
    private final MemberService memberService;
    private final PostService postService;

    @Bean
    @Order(3)
    public ApplicationRunner initNotProd() {
        return args -> {
            if (memberService.findByUsername("user1").isPresent()) {
                return;
            }

            self.work1();
        };
    }

    @Transactional
    public void work1() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Member member = memberService.join("user" + i, "1234").getData();
            if (i % 2 == 0) {
                member.setPaid(true);
            }
        });

        IntStream.rangeClosed(1, 100).forEach(i -> {
            Member member = memberService.findByUsername("user" + i).get();
            Post write = postService.write(member, "제목 " + i, "내용 " + i, true, false);
            if (i % 2 == 0) {
                write.setPaid(true);
            }
        });
    }
}