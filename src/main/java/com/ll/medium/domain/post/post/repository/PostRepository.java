package com.ll.medium.domain.post.post.repository;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.post.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post>findTop30ByIsPublishedOrderByIdDesc(boolean isPublished);

    Page<Post> findByIsPublishedOrderByIdDesc(boolean isPublished, Pageable pageable);

    List<Post> findByAuthor(Member author);

    Page<Post> findByIsPublishedAndAuthor(boolean isPublished, Member author, Pageable pageable);

    Optional<Post> findByIsPublishedAndId(boolean isPublished, long id);
}
