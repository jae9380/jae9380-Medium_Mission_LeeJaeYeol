package com.ll.medium.domain.member.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")
    public String showJoin(){
        return "domain/member/member/join";
    }
}
