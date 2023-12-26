package com.ll.medium.domain.post.postComment.service;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.postComment.entity.Comment;
import com.ll.medium.domain.post.postComment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    @Transactional
    public Comment write(Member member, Post post, String body) {
        return post.writeComment(member,body);
    }

    public boolean canModify(Member member, Comment comment){
        if (member == null) return false;

        return member.equals(comment.getMember());
    }

    public boolean canDelete(Member member, Comment comment){
        if (member == null) return false;

        if (member.isAdmin())return true;

        return member.equals(comment.getMember());
    }

    public Optional<Comment> findById(long id) {
        return commentRepository.findByid(id);
    }

    @Transactional
    public void modifyComment(Comment comment, String body) {
        comment.setBody(body);
    }

    @Transactional
    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }
}
