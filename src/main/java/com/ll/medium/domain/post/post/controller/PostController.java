package com.ll.medium.domain.post.post.controller;

import com.ll.medium.domain.member.member.entity.Member;
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
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final Rq rq;

    @GetMapping("list")
    public String showList(
            @RequestParam(value = "kwType",defaultValue = "")String kwType,
            @RequestParam(defaultValue = "")String kw,
            @RequestParam(value = "page",defaultValue = "0")int page
    ){

        rq.setAttribute("posts",postService.search(kwType,kw,page));
        return "domain/post/post/list";
    }

    @GetMapping("/myList")
    public String showMyList(@RequestParam(value = "page",defaultValue = "0")int page){
        rq.setAttribute("posts",postService.findByAuthor(rq.getMember(),page));
        return "domain/post/post/myList";
    }

    @GetMapping("/{id}")
    public String showPost( @PathVariable long id){
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String showWrite(){
        return "domain/post/post/write";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String write(@Valid WriteForm writeForm){
        Member member=this.rq.getMember();
        Post post = postService.write(member,writeForm.getTitle(),writeForm.getBody(), writeForm.isPublished(),writeForm.isPublished());
        return rq.redirect("/","%d번 게시글 작성을 완료했습니다.".formatted(post.getId()));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/modify")
    public String showModify(@PathVariable long id){
        Post post=postService.findById(id).get();

        if (!postService.canModify(rq.getMember(),post)) rq.redirect("/","수정 권한이 없습니다.");
        rq.setAttribute("post",post);

        return "domain/post/post/modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}/modify")
    public String modify(@PathVariable long id, @Valid ModifyForm modifyForm){
        Post post=postService.findById(id).get();
        if (!postService.canModify(rq.getMember(),post)) throw new RuntimeException("수정권한이 없습니다.");
        postService.modify(post,modifyForm);
        return rq.redirect("/","%d번 게시물 수정되었습니다.".formatted(id));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}/delete")
    public String delete(@PathVariable long id){
        Post post = postService.findById(id).get();
        if (!postService.canDelete(rq.getMember(),post))throw new RuntimeException("삭제 권한이 없습니다.");
        postService.delete(post);
        return rq.redirect("/","%d번 글이 삭제되었습니다.".formatted(id));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/like")
    public String like(@PathVariable long id){
        Post post = postService.findById(id).get();

        if (!postService.canLike(rq.getMember(),post))throw new RuntimeException("권한이 없습니다.");

        postService.like(rq.getMember(),post);

        return rq.redirect("/post/"+post.getId(),post.getId()+"번 글 추천했습니다.");
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}/cancelLike")
    public String cancelLike(@PathVariable long id){
        Post post = postService.findById(id).get();

        if (!postService.canCancelLike(rq.getMember(),post))throw new RuntimeException("권한이 없습니다.");

        postService.cancelLike(rq.getMember(),post);

        return rq.redirect("/post/"+post.getId(),post.getId()+"번 글 추천 취소했습니다.");
    }

    @Data
    public static class WriteForm{
        @NotBlank
        private String title;
        @NotBlank
        private String body;
        private boolean published;
        private boolean paid;
    }

    @Data
    public static class ModifyForm {
        @NotBlank
        private String title;
        @NotBlank
        private String body;
        private boolean published;
        private boolean paid;
    }
}
