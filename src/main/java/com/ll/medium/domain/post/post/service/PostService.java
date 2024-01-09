package com.ll.medium.domain.post.post.service;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.post.post.controller.PostController;
import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.entity.PostDetail;
import com.ll.medium.domain.post.post.repository.PostDetailRepository;
import com.ll.medium.domain.post.post.repository.PostRepository;
import com.ll.medium.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final PostDetailRepository postDetailRepository;
    private final Rq rq;

    @Transactional
    public Post write(Member author, String title, String body, boolean isPublished, boolean isPaid){
        Post post = Post.builder()
                .author(author).title(title)
                .body(body).isPublished(isPublished)
                .isPaid(isPaid)
                .build();
        postRepository.save(post);

        saveBody(post, body);

        return post;
    }

    public Page<Post> findTop10ByIsPublishedOrderByIdDesc(boolean isPublished,int page) {
        Pageable pageable = PageRequest.of(page,10, Sort.by("id").descending());
        return postRepository.findTop10ByIsPublishedOrderByIdDesc(isPublished,pageable);
    }

    public Page<Post> search(String kwType, String kw,String sort, int page){
        Pageable pageable = PageRequest.of(page,10);
        return postRepository.search(kwType,kw,sort,pageable);
    }

    public Page<Post> search(String kwType, String kw,String sort, int page, String username){
        Member member = rq.getMember(username);
        Pageable pageable = PageRequest.of(page,10);
        return postRepository.search(kwType,kw,sort,pageable,member);
    }

    public Page<Post> findByAuthor(Member author,int page){
        Pageable pageable = PageRequest.of(page,10, Sort.by("id").descending());
        return postRepository.findByAuthor(author,pageable);
    }

    public Page<Post> findByIsPublishedAndAuthor(boolean isPublished,Member author, int page){
        Pageable pageable = PageRequest.of(page,10, Sort.by("id").descending());
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
    public void edit(Post post, PostController.EditForm form) {
    post.setTitle(form.getTitle());
    post.setPublished(form.isPublished());
    post.setPaid(form.isPaid());

    saveBody(post, form.getBody());
    }

    private void saveBody(Post post, String body) {
        post.setBody(body);
        PostDetail postDetailBody = findDetail(post,"common__body");
        postDetailBody.setVal(body);
        post.setPostDetailBody(postDetailBody);
    }

    private PostDetail findDetail(Post post, String name){
        Optional<PostDetail> opPOstDetailBody = postDetailRepository.findByPostAndName(post,name);

        PostDetail postDetailBody = opPOstDetailBody.orElseGet(() -> postDetailRepository.save(
                PostDetail.builder()
                        .post(post).name("common__body").build()
        ));
        return postDetailBody;
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
        if (post.isPaid()&&!member.isPaid()) return false;
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

    @Transactional
    public void increaseHit(Post post) {
        post.increaseHit();
    }

    public Post finTempOrMake(Member author) {
        return postRepository.findByAuthorAndIsPublishedAndTitle(author,false,"임시글")
        .orElseGet(() -> write(author, "임시글", "", false,false));
    }
}
