package com.ll.medium.domain.post.post.entity;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.post.postComment.entity.Comment;
import com.ll.medium.domain.post.postLike.entity.PostLike;
import com.ll.medium.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Builder
@Getter
@Setter
public class Post extends BaseEntity {
    @OneToMany(mappedBy="post", cascade = ALL, orphanRemoval = true)
    @Builder.Default
    private List<PostLike> likes = new ArrayList<>();
    @OneToMany(mappedBy="post", cascade = ALL, orphanRemoval = true)
    @Builder.Default
    @OrderBy("id DESC")
    private List<Comment> cmt = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    private Member author;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String body;
    private boolean isPublished;
    private boolean isPaid;

    public void addLike(Member member){
        if(hasLike(member)) return;
        likes.add(PostLike.builder()
                .post(this)
                .member(member)
                .build());
    }

    public void deleteLike(Member member){
        likes.removeIf(postLike -> postLike.getMember().equals(member));
    }

    public boolean hasLike (Member member){
        return likes.stream().anyMatch(postLike -> postLike.getMember().equals(member));
    }

    public Comment writeComment(Member member, String body) {
        Comment comment = Comment.builder()
                .post(this).member(member)
                .body(body).build();
        cmt.add(comment);
        return comment;
    }
}