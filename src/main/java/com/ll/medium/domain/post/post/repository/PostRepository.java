package com.ll.medium.domain.post.post.repository;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.post.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findTop10ByIsPublishedOrderByIdDesc(boolean isPublished,Pageable pageable);

    Page<Post> findByIsPublishedOrderByIdDesc(boolean isPublished, Pageable pageable);

    Page<Post> findByAuthor(Member author, Pageable pageable);

    Page<Post> findByIsPublishedAndAuthor(boolean isPublished, Member author, Pageable pageable);

    Optional<Post> findByIsPublishedAndId(boolean isPublished, long id);

    Page<Post> findByIsPublishedAndTitleContaining(boolean isPublished, String kw, Pageable pageable);

    Page<Post> findByIsPublishedAndBodyContaining(boolean isPublished, String kw, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.isPublished = true AND (p.title LIKE %:kw% OR p.body LIKE %:kw%)")
    Page<Post> findByIsPublishedAndTitleContainingOrBodyContaining(@Param("kw") String kw, Pageable pageable);

    Page<Post> findByIsPublished(boolean isPublished, Pageable pageable);

    Page<Post> findByIsPublishedAndAuthorUsernameContaining(boolean isPublished, String kw, Pageable pageable);
}
