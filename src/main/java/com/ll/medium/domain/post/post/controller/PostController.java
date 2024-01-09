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
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final Rq rq;

    @GetMapping("list")
    public String showList(
            @RequestParam(value = "kwType",defaultValue = "")String kwType,
            @RequestParam(defaultValue = "")String kw,
            @RequestParam(value = "sortCode",defaultValue = "idDesc")String sortCode,
            @RequestParam(value = "page",defaultValue = "0")int page
    ){
        rq.setAttribute("posts",postService.search(kwType,kw,sortCode,page));
        rq.setAttribute("sortCode",sortCode);
        rq.setAttribute("kwType",kwType);
        rq.setAttribute("kw",kw);

        return "domain/post/post/list";
    }

    @GetMapping("/myList")
    public String showMyList(@RequestParam(value = "page",defaultValue = "0")int page){
        if (rq.isLogout()){
            return rq.redirect("/", "로그인 후 이용 가능합니다.","warning");
        }
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
             return rq.redirect("/","열람권한이 없습니다.","warring");
            }
        }
        postService.increaseHit(post);
            rq.setAttribute("post",post);
        return "domain/post/post/detail";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/makeTemp")
    public String makeTemp(){
        Post post = postService.finTempOrMake(rq.getMember());
        return rq.redirect("/post/%d/edit".formatted(post.getId()),post.getId()+"번 임시글이 생성되었습니다.","success");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/edit")
    public String showEdit(@PathVariable long id){
        Post post=postService.findById(id).orElseThrow(()->new RuntimeException("해당 글은 존재하지 않습니다."));

        if (!postService.canModify(rq.getMember(),post)) rq.redirect("/","수정 권한이 없습니다.","warning");
        rq.setAttribute("post",post);

        return "domain/post/post/edit";
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}/edit")
    public String edit(@PathVariable long id, @Valid PostController.EditForm form){
        Post post=postService.findById(id).orElseThrow(()->new RuntimeException("해당 글은 존재하지 않습니다."));
        if (!postService.canModify(rq.getMember(),post)) throw new RuntimeException("수정권한이 없습니다.");
        postService.edit(post,form);
        return rq.redirect("/","%d번 게시물 수정되었습니다.".formatted(id),"success");
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}/delete")
    public String delete(@PathVariable long id){
        Post post = postService.findById(id).get();
        if (!postService.canDelete(rq.getMember(),post))throw new RuntimeException("삭제 권한이 없습니다.");
        postService.delete(post);
        return rq.redirect("/","%d번 글이 삭제되었습니다.".formatted(id),"success");
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/like")
    public String like(@PathVariable long id){
        Post post = postService.findById(id).get();

        if (!postService.canLike(rq.getMember(),post))throw new RuntimeException("권한이 없습니다.");

        postService.like(rq.getMember(),post);

        return rq.redirect("/post/"+post.getId(),post.getId()+"번 글 추천했습니다.","success");
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}/cancelLike")
    public String cancelLike(@PathVariable long id){
        Post post = postService.findById(id).get();

        if (!postService.canCancelLike(rq.getMember(),post))throw new RuntimeException("권한이 없습니다.");

        postService.cancelLike(rq.getMember(),post);

        return rq.redirect("/post/"+post.getId(),post.getId()+"번 글 추천 취소했습니다.","success");
    }

    @Data
    public static class EditForm {
        @NotBlank
        private String title;
        @NotBlank
        private String body;
        private boolean published;
        private boolean paid;
    }
}
