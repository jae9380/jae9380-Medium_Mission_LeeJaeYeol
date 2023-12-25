package com.ll.medium.domain.post.postComment.repository;

import com.ll.medium.domain.post.postComment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

    Optional<Comment> findByid(long id);
}
