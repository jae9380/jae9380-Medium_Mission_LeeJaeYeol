package com.ll.medium.domain.post.post.controller;

import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.service.PostService;
import com.ll.medium.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/b")
public class BlogController {
    private final PostService postService;
    private final Rq rq;

    @GetMapping("/{username}")
    public String showListByUser(@PathVariable String username, @RequestParam(value = "page",defaultValue = "0")int page){
        rq.setAttribute("posts",postService.findByIsPublishedAndAuthor(true,rq.getMember(username),page
        ));
        return "domain/post/post/listByMember";
    }

    @GetMapping("/{username}/{id}")
    public String showPost( @PathVariable String username, @PathVariable long id){

                Post post = postService.findById(id).get();
        if (!post.isPublished()){
            if (postService.canModify(rq.getMember(),post)){
                post=postService.findById(id).get();
            }else {
                return rq.redirect("/","열람권한이 없습니다.");
            }
        }
        rq.setAttribute("post",post);
        return "domain/post/post/detail";
    }
}
