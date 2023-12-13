package com.ll.medium.domain.post.post.service;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.post.post.controller.PostController;
import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;

    @Transactional
    public Post write(Member author, String title, String body, boolean isPublished){
        Post post = Post.builder()
                .author(author).title(title)
                .body(body).isPublished(isPublished)
                .build();
        postRepository.save(post);

        return post;
    }

    public Object findTop30ByIsPublishedOrderByIdDesc(boolean isPublished) {
        return postRepository.findTop30ByIsPublishedOrderByIdDesc(isPublished);
    }

    public Object findByIsPublishedOrderByIdDesc(boolean isPublished){
        return postRepository.findByIsPublishedOrderByIdDesc(isPublished);
    }

    public Object findByAuthor(Member author){
        return postRepository.findByAuthor(author);
    }

    public Optional<Post> findById(long id) {
        return postRepository.findById(id);
    }

    public boolean canModify(Member member, Post post) {
        if (member==null)return false;
        return post.getAuthor().equals(member);
    }

    @Transactional
    public void modify(Post post, PostController.ModifyForm modifyForm) {
    post.setTitle(modifyForm.getTitle());
    post.setBody(modifyForm.getBody());
    post.setPublished(modifyForm.isPublished());
    }

    public boolean canDelete(Member member, Post post) {
        if (member==null)return false;
        if (member.isAdmin())return true;
        return post.getAuthor().equals(member);
    }

    @Transactional
    public void delete(Post post) {
        postRepository.delete(post);
    }
}
