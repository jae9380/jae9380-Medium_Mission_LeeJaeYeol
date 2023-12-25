package com.ll.medium.domain.post.post.service;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.post.post.controller.PostController;
import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.repository.PostRepository;
import com.ll.medium.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final Rq rq;

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

    public Page<Post> search(String kwType, String kw, int page){
        Pageable pageable = PageRequest.of(page,10);
        if ("title".equals(kwType)){
            return postRepository.findByIsPublishedAndTitleContaining(true, kw,pageable);
        } else if ("body".equals(kwType)) {
            return postRepository.findByIsPublishedAndBodyContaining(true, kw,pageable);
        } else if ("authorUsername".equals(kwType)) {
            Member member=rq.getMember(kw);
            return postRepository.findByIsPublishedAndAuthor(true,member,pageable);
        }else if ("titleAndBody".equals(kwType)){
            return postRepository.findByIsPublishedAndTitleContainingOrBodyContaining(kw,pageable);
        }else {
            return postRepository.findByIsPublishedOrderByIdDesc(true, pageable);
        }
    }

    public Page<Post> findByAuthor(Member author,int page){
        Pageable pageable = PageRequest.of(page,10);
        return postRepository.findByAuthor(author,pageable);
    }

    public Page<Post> findByIsPublishedAndAuthor(boolean isPublished,Member author, int page){
        Pageable pageable = PageRequest.of(page,10);
        return postRepository.findByIsPublishedAndAuthor(true,author,pageable);
    }

    public Optional<Post> findById(long id) {
        return postRepository.findById(id);
    }

    public Optional<Post> findByIsPublishedAndId(boolean isPublished,long id) {
        return postRepository.findByIsPublishedAndId(true,id);
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

    public boolean canLike(Member member, Post post) {
        if (member==null)return false;
        return !post.hasLike(member);
    }

    public boolean canCancelLike(Member member, Post post) {
        if (member==null)return false;
        return post.hasLike(member);
    }

    @Transactional
    public void like(Member member, Post post) {
        post.addLike(member);
    }

    @Transactional
    public void cancelLike(Member member, Post post) {
        post.deleteLike(member);
    }
}
