package com.ll.medium.domain.post.post.repository;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.post.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<Post> search(String kwTypes, String kw, String sort, Pageable pageable, Member... member);
}
