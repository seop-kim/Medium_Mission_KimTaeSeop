package com.ll.medium.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/member")
@RequiredArgsConstructor
@Controller
public class MemberController {

    // == 회원가입 폼 ==
    @GetMapping("/join")
    public String joinForm() {
        return "/member/joinForm";
    }

    // == 회원가입 ==
    @PostMapping("/join")
    @ResponseBody
    public String join() {
        return "회원가입 처리가 필요합니다.";
    }

    // == 로그인 폼 ==
    @GetMapping("/login")
    public String login() {
        return "/member/login";
    }
}
