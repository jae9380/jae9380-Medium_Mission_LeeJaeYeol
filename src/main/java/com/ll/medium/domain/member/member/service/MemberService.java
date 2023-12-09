package com.ll.medium.domain.member.member.service;

import com.ll.medium.domain.member.member.controller.MemberController;
import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member join(MemberController.JoinForm joinForm){
        if (findByUsername(joinForm.getUsername()).isPresent()){
            return null;
        }
        Member member = Member.builder()
                .username(joinForm.getUsername())
                .password(passwordEncoder.encode(joinForm.getPassword()))
                .build();
        memberRepository.save(member);
        return member;
    }
    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }
}
