package com.ll.medium.global.initData;

import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.global.app.AppConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.io.File;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class All {
    private final MemberService memberService;

    @Bean
    @Order(1)
    public ApplicationRunner initAll() {
        return args -> {

            new File(AppConfig.getTempDirPath()).mkdirs();

            if (memberService.findByUsername("admin").isPresent() && memberService.findByUsername("system").isPresent()) return;
            memberService.join("admin", "1234",true);
            memberService.join("system", "1234",false);

        };
    }
}