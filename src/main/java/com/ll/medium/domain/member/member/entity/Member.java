package com.ll.medium.domain.member.member.entity;

import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.global.jpa.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@ToString(callSuper = true)
public class Member extends BaseEntity {
    private String username,password;
    @OneToMany(cascade = CascadeType.REMOVE)
    private List<Post> posts;
    private boolean isPaid;

    public boolean isAdmin() {
        return username.equals("admin");
    }

    public Collection<? extends GrantedAuthority> getAuthorities(){
        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));

        if (List.of("system","admin").contains(username)){
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        if (this.isPaid()){
            authorities.add(new SimpleGrantedAuthority("ROLE_PAID"));
        }
        return authorities;
    }
}
