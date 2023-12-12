package com.ll.medium.domain.post.post.controller;

import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.domain.post.post.service.PostService;
import com.ll.medium.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final MemberService memberService;
    private final Rq rq;

    @GetMapping("/list")
    public String showList(){
        rq.setAttribute("posts",postService.findByIsPublishedOrderByIdDesc(true));
        return "domain/post/post/list";
    }

    @GetMapping("/myList")
    public String showMyList(){
        rq.setAttribute("posts",postService.findByAuthor(rq.getMember()));
        return "domain/post/post/myList";
    }
}
