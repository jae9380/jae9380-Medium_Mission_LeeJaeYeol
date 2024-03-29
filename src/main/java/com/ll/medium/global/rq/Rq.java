package com.ll.medium.global.rq;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.global.rsData.RsData;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@RequestScope
@Component
@RequiredArgsConstructor
public class Rq {
    private final HttpServletRequest req;
    private final HttpServletResponse resp;
    private final MemberService memberService;
    private User user;
    private Member member;

    @PostConstruct
    public void init() {
        // 현재 로그인한 회원의 인증정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof User) {
            this.user = (User) authentication.getPrincipal();
        }
    }

    public String redirect(String url, String msg, String... code) {
        String[] urlBits = url.split("#", 2);
        url = urlBits[0];

        msg = URLEncoder.encode(msg, StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder();
        sb.append("redirect:");
        sb.append(url);

        if (code!=null&&code[0]!=null&&msg!=null){
            switch (code[0]){
                case "info":
                    sb.append("?infoMsg=");
                    sb.append(msg);
                    break;
                case "success":
                    sb.append("?successMsg=");
                    sb.append(msg);
                    break;
                case "warning":
                    sb.append("?warningMsg=");
                    sb.append(msg);
                    break;
                case "error":
                    sb.append("?errorMsg=");
                    sb.append(msg);
                    break;
            }
        }else {
        if (msg != null) {
            sb.append("?successMsg=");
            sb.append(msg);
        }}
        if (urlBits.length == 2) {
            sb.append("#");
            sb.append(urlBits[1]);
        }

        return sb.toString();
    }

    public String redirect(String path, RsData<?> rs) {
        return redirect(path, rs.getMsg());
    }

    public String historyBack(String msg) {
        req.setAttribute("msg", msg);
        return "global/historyBack";
    }

    public String redirectOrBack(String url, RsData<?> rs) {
        if (rs.isFail()) return historyBack(rs.getMsg());
        return redirect(url, rs);
    }

    public User getUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .filter(it -> it instanceof User)
                .map(it -> (User) it)
                .orElse(null);
    }

    public boolean isLogin() {
        return getUser() != null;
    }

    public boolean isLogout() {
        return !isLogin();
    }

    public boolean isAdmin() {
        if (isLogout()) return false;

        return getUser()
                .getAuthorities()
                .stream()
                .anyMatch(it -> it.getAuthority().equals("ROLE_ADMIN"));
    }

    public void setAttribute(String key,Object value){
        req.setAttribute(key, value);
    }

    private String getMemberUsername() {
        return user.getUsername();
    }

    public Member getMember() {
        if (!isLogin()) {
            return null;
        }

        if (member == null)
            member = memberService.findByUsername(getMemberUsername()).get();

        return member;
    }

    public Member getMember(String username){
        member=memberService.findByUsername(username).get();
        return member;
    }

    public String getProfileImgUrl() {
        return memberService.getProfileImgUrl(getMember());
    }

    public String getReferer() {
        return req.getHeader("referer");
    }
}
