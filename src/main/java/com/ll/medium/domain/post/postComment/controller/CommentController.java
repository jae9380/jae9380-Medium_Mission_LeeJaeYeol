package com.ll.medium.domain.post.postComment.controller;

import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.service.PostService;
import com.ll.medium.domain.post.postComment.entity.Comment;
import com.ll.medium.domain.post.postComment.service.CommentService;
import com.ll.medium.global.rq.Rq;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/post/{id}/comment")
@RequiredArgsConstructor
public class CommentController {
    private final PostService postService;
    private final CommentService commentService;
    private final Rq rq;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String write(@PathVariable long id, @Valid WriteForm writeForm){
        Post post = postService.findById(id).get();
        Comment comment = commentService.write(rq.getMember(),post, writeForm.getBody());

        return rq.redirect("/post/"+id+"#postCmt-"+comment.getId(),"댓글이 등록되었습니다.");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{commentId}/modify")
    public String showModify(@PathVariable long id,@PathVariable long commentId){
        Post post = postService.findById(id).orElseThrow(()->new RuntimeException("해당 글은 없습니다."));
        Comment comment = commentService.findById(commentId).orElseThrow(()->new RuntimeException("해당 댓글은 없습니다."));

        if(!commentService.canModify(rq.getMember(),comment)) throw new RuntimeException("권한이 없습니다.");

        rq.setAttribute("post",post);
        rq.setAttribute("postComment",comment);

        return "/domain/post/post/modifyComment";
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{commentId}/modify")
    public String modify(@PathVariable long id,@PathVariable long commentId, @Valid ModifyForm modifyForm){
        Comment comment = commentService.findById(commentId).orElseThrow(()->new RuntimeException("해당 댓글은 없습니다."));

        commentService.modifyComment(comment, modifyForm.getBody());

        return rq.redirect("/post/"+id+"#postCmt-"+comment.getId(),"댓글이 수정되었습니다.");
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{commentId}/delete")
    public String delete(@PathVariable long id,@PathVariable long commentId){
        Comment comment = commentService.findById(commentId).orElseThrow(()->new RuntimeException("해당 댓글은 없습니다."));

        if(!commentService.canDelete(rq.getMember(),comment)) throw new RuntimeException("권한이 없습니다.");

        commentService.deleteComment(comment);

        return rq.redirect("/post/"+id,commentId+"번 댓글이 삭제되었습니다.");
    }

    @Data
    public static class WriteForm{
        @NotBlank
        private String body;
    }

    @Data
    public static class ModifyForm{
        @NotBlank
        private String body;
    }
}
