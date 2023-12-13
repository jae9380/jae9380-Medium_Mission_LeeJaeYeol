package com.ll.medium.domain.post.post.repository;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.post.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post>findTop30ByIsPublishedOrderByIdDesc(boolean isPublished);

    List<Post> findByIsPublishedOrderByIdDesc(boolean isPublished);

    List<Post> findByAuthor(Member author);

    List<Post> findByIsPublishedAndAuthor(boolean isPublished,Member author);

    Optional<Post> findByIsPublishedAndId(boolean isPublished, long id);
}
