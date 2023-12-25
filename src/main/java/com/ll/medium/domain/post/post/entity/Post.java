package com.ll.medium.domain.post.post.entity;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.post.postComment.entity.PostCmt;
import com.ll.medium.domain.post.postLike.entity.PostLike;
import com.ll.medium.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
    private List<PostCmt> cmt = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    private Member author;
    private String title;
    private String body;
    private boolean isPublished;

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

    public PostCmt writeCmt(Member member, String body) {
        PostCmt postCmt = PostCmt.builder()
                .post(this).member(member)
                .body(body).build();
        cmt.add(postCmt);
        return postCmt;
    }
}