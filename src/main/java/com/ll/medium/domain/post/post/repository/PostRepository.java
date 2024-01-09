package com.ll.medium.domain.post.post.repository;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.post.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    Page<Post> findTop10ByIsPublishedOrderByIdDesc(boolean isPublished,Pageable pageable);

    Page<Post> findByAuthor(Member author, Pageable pageable);

    Page<Post> findByIsPublishedAndAuthor(boolean isPublished, Member author, Pageable pageable);

    Optional<Post> findByIsPublishedAndId(boolean isPublished, long id);

    Page<Post> findByIsPublished(boolean isPublished, Pageable pageable);

    Optional<Post> findByAuthorAndIsPublishedAndTitle(Member author, boolean isPublished, String 임시글);
}
