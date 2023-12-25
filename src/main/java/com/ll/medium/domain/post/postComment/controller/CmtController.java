package com.ll.medium.domain.post.postComment.controller;

import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.service.PostService;
import com.ll.medium.domain.post.postComment.entity.PostCmt;
import com.ll.medium.domain.post.postComment.service.CmtService;
import com.ll.medium.global.rq.Rq;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/post/{id}/comment")
@RequiredArgsConstructor
public class CmtController {
    private final PostService postService;
    private final CmtService cmtService;
    private final Rq rq;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String write(@PathVariable long id, @Valid WriteForm writeForm){
        Post post = postService.findById(id).get();
        PostCmt postCmt = cmtService.write(rq.getMember(),post, writeForm.getBody());

        return rq.redirect("/post/"+id+"#postCmt-"+postCmt.getId(),"댓글이 등록되었습니다.");
    }

    @Data
    public static class WriteForm{
        @NotBlank
        private String body;
    }
}
