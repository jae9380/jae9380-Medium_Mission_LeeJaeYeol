package com.ll.medium.domain.member.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/adm")
@RequiredArgsConstructor
public class AdminController {

    @GetMapping("")
    public String showMain(){
        return "domain/home/home/adm/main";
    }

    @GetMapping("/home/about")
    public String showAbout(){
        return "domain/home/home/adm/about";
    }
}
