package com.ll.medium.global.rq;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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

    public String redirect(String url, String msg) {
        msg = URLEncoder.encode(msg, StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder();
        sb.append("redirect:");
        sb.append(url);
        if (msg != null) {
            sb.append("?msg=");
            sb.append(msg);
        }
        return sb.toString();
    }

    public String historyBack(String msg) {
        req.setAttribute("msg", msg);
        return "global/historyBack";
    }
}
