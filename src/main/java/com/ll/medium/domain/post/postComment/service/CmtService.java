package com.ll.medium.domain.post.postComment.service;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.postComment.entity.PostCmt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CmtService {
    @Transactional
    public PostCmt write(Member member, Post post, String body) {
        return post.writeCmt(member,body);
    }
}
