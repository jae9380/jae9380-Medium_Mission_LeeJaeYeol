package com.ll.medium.domain.post.post.controller;

import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.service.PostService;
import com.ll.medium.global.rq.Rq;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final Rq rq;

    @GetMapping("/post/list")
    public String showList(){
        rq.setAttribute("posts",postService.findByIsPublishedOrderByIdDesc(true));
        return "domain/post/post/list";
    }

    @GetMapping("/post/myList")
    public String showMyList(){
        rq.setAttribute("posts",postService.findByAuthor(rq.getMember()));
        return "domain/post/post/myList";
    }

    @GetMapping({"/post/{id}","/b/{username}/{id}"})
    public String showPost(@PathVariable(required = false) String username, @PathVariable long id){
        Post post = postService.findById(id).get();

        if (!post.isPublished()){
            if (!postService.canModify(rq.getMember(),post)){
                throw new RuntimeException("열람 권한이 없습니다.");
            }
        }
            rq.setAttribute("post",post);
        return "domain/post/post/detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/post/write")
    public String showWrite(){
        return "domain/post/post/write";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/post/write")
    public String write(@Valid WriteForm writeForm){
        Post post = postService.write(rq.getMember(),writeForm.getTitle(),writeForm.getBody(), writeForm.isPublished());
        return rq.redirect("/","%d번 게시글 작성을 완료했습니다.".formatted(post.getId()));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/post/{id}/modify")
    public String showModify(@PathVariable long id){
        Post post=postService.findById(id).get();

        if (!postService.canModify(rq.getMember(),post)) rq.redirect("/","수정 권한이 없습니다.");
        rq.setAttribute("post",post);

        return "domain/post/post/modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/post/{id}/modify")
    public String modify(@PathVariable long id, @Valid ModifyForm modifyForm){
        Post post=postService.findById(id).get();
        if (!postService.canModify(rq.getMember(),post)) throw new RuntimeException("수정권한이 없습니다.");
        postService.modify(post,modifyForm);
        return rq.redirect("/","%d번 게시물 수정되었습니다.".formatted(id));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/post/{id}/delete")
    public String delete(@PathVariable long id){
        Post post = postService.findById(id).get();
        if (!postService.canDelete(rq.getMember(),post))throw new RuntimeException("삭제 권한이 없습니다.");
        postService.delete(post);
        return rq.redirect("/","%d번 글이 삭제되었습니다.".formatted(id));
    }

    @GetMapping("/b/{username}")
    public String showListByUser(@PathVariable String username){
        rq.setAttribute("posts",postService.findByIsPublishedAndAuthor(true,rq.getMember(username)));
        return "domain/post/post/listbyusername";
    }

//    @GetMapping("/b/{username}/{id}")
//    public String showDetailByUser(@PathVariable String username){
//        rq.setAttribute("posts",postService.findByIsPublishedAndAuthor(true,rq.getMember(username)));
//        return "domain/post/post/listbyusername";
//    }

    @Data
    public static class WriteForm{
        @NotBlank
        private String title;
        @NotBlank
        private String body;
        private boolean published;
    }

    @Data
    public static class ModifyForm {
        @NotBlank
        private String title;
        @NotBlank
        private String body;
        private boolean published;
    }


}
