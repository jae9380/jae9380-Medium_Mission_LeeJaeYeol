package com.ll.medium.domain.home.home.controller;

import com.ll.medium.domain.post.post.service.PostService;
import com.ll.medium.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor

public class HomeController {
    private final Rq rq;
    private final PostService postService;
    @GetMapping("/")
    public String showMain(@RequestParam(value = "page",defaultValue = "0")int page){
        page= page>2?2:page;
        rq.setAttribute("posts",postService.findTop10ByIsPublishedOrderByIdDesc(true, page));
        return "domain/home/home/main";
    }
}
