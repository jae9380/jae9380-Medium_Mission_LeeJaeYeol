package com.ll.medium.domain.member.member.service;

import com.ll.medium.domain.member.member.controller.MemberController;
import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.repository.MemberRepository;
import com.ll.medium.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RsData<Member> join(MemberController.JoinForm joinForm){
        if (List.of("admin","system").contains(joinForm.getUsername())){
            return RsData.of("400-1","\"%s\" 해당 아이디는 사용이 불가능 합니다. ".formatted(joinForm.getUsername()));
        }
        if (findByUsername(joinForm.getUsername()).isPresent()){
            return RsData.of("400-2","이미 존재하는 아이디 입니다.");
        }
        Member member = Member.builder()
                .username(joinForm.getUsername())
                .password(passwordEncoder.encode(joinForm.getPassword()))
                .build();
        memberRepository.save(member);
        return RsData.of("200","\"%s\"님 환영합니다.".formatted(member.getUsername()),member);
    }
    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }
}
