package com.ll.medium.domain.post.post.repository;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.entity.QPost;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;


@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Page<Post> search(String kwTypes, String kw, String sort, Pageable pageable, Member... member) {
        QPost post = QPost.post;
        BooleanBuilder builder = new BooleanBuilder();


        switch (kwTypes){
            case "title": builder.or(post.title.containsIgnoreCase(kw));
                break;
            case "body": builder.or(post.body.containsIgnoreCase(kw));
                break;
            case "authorUsername": builder.or(post.author.username.containsIgnoreCase(kw));
                break;
            case "titleAndBody":
                builder.or(post.title.containsIgnoreCase(kw));
                builder.or(post.body.containsIgnoreCase(kw));
                break;
            default:
        }

        builder.and(post.isPublished.isTrue());

        if (member.length>0){
            builder.and(post.author.username.eq(member[0].getUsername()));
        }

        JPAQuery<Post> query = jpaQueryFactory
                .selectFrom(post)
                .where(builder);

        if (sort.equals("idDesc")) {
            query.orderBy(post.id.desc());
        } else if (sort.equals("idAsc")) {
            query.orderBy(post.id.asc());
        } else if (sort.equals("hitDesc")) {
            query.orderBy(post.hit.desc());
        } else if (sort.equals("hitAsc")) {
            query.orderBy(post.hit.asc());
        } else if (sort.equals("likesDesc")) {
            query.orderBy(post.likes.size().desc());
        } else if (sort.equals("likesAsc")) {
            query.orderBy(post.likes.size().asc());
        }

        QueryResults<Post> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }
}
