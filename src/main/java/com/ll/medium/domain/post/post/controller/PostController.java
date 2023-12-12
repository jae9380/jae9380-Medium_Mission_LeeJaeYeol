package com.ll.medium.domain.post.post.controller;

import com.ll.medium.domain.post.post.service.PostService;
import com.ll.medium.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
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

    @GetMapping("/{id}")
    public String showPost(@PathVariable long id){
        rq.setAttribute("post",postService.findById(id).get());
        return "domain/post/post/detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String showWrite(){
        return "domain/post/post/write";
    }
}
