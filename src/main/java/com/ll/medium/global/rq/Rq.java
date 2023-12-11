package com.ll.medium.global.rq;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.global.rsData.RsData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RequestScope
@Component
@RequiredArgsConstructor
public class Rq {
    private final HttpServletRequest req;
    private final HttpServletResponse resp;
    private final MemberService memberService;
    private User user;
    private Member member;

    public String redirect(String url, String msg) {
        msg = URLEncoder.encode(msg, StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder();
        sb.append("redirect:");
        sb.append(url);
        if (msg != null) {
            sb.append("?successMsg=");
            sb.append(msg);
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

    public String historyBack(RsData<?> rs) {
        return historyBack(rs.getMsg());
    }


    public boolean isLogined() {
        return user != null;
    }

    private String getMemberUsername() {
        return user.getUsername();
    }

    public Member getMember() {
        if (!isLogined()) {
            return null;
        }

        if (member == null) member = memberService.findByUsername(getMemberUsername()).get();

        return member;
    }

    public String redirectOrBack(String url, RsData<?> rs) {
        if (rs.isFail()) return historyBack(rs);
        return redirect(url, rs);
    }
}
