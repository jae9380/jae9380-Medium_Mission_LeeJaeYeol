package com.ll.medium.global.initData;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.service.PostService;
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

import java.util.Random;
import java.util.stream.IntStream;

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
    @Order(2)
    public ApplicationRunner initNotProd() {
        return args -> {
            if (memberService.findByUsername("user1").isPresent() && memberService.findByUsername("user2").isPresent()) return;
            self.work1();
        };
    }

    @Transactional
    public void work1(){
        Member member1 = memberService.join("user1","1234",false).getDat();
        Member member2 = memberService.join("user2","1234",true).getDat();
        Member member3 = memberService.join("user3","1234",false).getDat();
        Member member4 = memberService.join("user4","1234",true).getDat();

        postService.write(member1,"제목1","내용1",true,false);
        postService.write(member2,"제목2","내용2",false,false);
        postService.write(member3,"제목3","내용3",true,true);
        postService.write(member3,"제목4","내용4",false,false);
        postService.write(member4,"제목5","내용5",true,false);
        Post post6= postService.write(member4,"제목6","내용6",true,false);

        Member[] members={member1,member2,member3,member4};

        IntStream.rangeClosed(7,207).forEach(i->{
            Member randomMember=members[new Random().nextInt(members.length)];
            boolean randomBoolean=new Random().nextBoolean();
            postService.write(randomMember,"제목"+i,"내용"+i,true,randomBoolean);
        });

        postService.like(member1,post6);
        postService.like(member2,post6);
        postService.like(member3,post6);
        postService.like(member1,post6);
    }
}