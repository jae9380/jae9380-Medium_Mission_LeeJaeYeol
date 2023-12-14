package com.ll.medium.global.initData;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.domain.post.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

import java.util.stream.IntStream;

@Configuration
@Profile("!prod")
@Slf4j
@RequiredArgsConstructor
public class NotProd {
    private final MemberService memberService;
    private final PostService postService;
    @Bean
    @Order(2)
    public ApplicationRunner initNotProd() {
        return args -> {
            if (memberService.findByUsername("user1").isPresent() && memberService.findByUsername("user2").isPresent()) return;
            Member member1 = memberService.join("user1","1234").getDat();
            Member member2 = memberService.join("user2","1234").getDat();
            Member member3 = memberService.join("user3","1234").getDat();
            Member member4 = memberService.join("user4","1234").getDat();

            postService.write(member1,"제목1","내용1",true);
            postService.write(member2,"제목2","내용2",false);
            postService.write(member3,"제목3","내용3",true);
            postService.write(member3,"제목4","내용4",false);
            postService.write(member4,"제목5","내용5",true);
            IntStream.rangeClosed(6,50).forEach(i->{postService.write(member4,"제목"+i,"내용"+i,true);});

        };
    }
}