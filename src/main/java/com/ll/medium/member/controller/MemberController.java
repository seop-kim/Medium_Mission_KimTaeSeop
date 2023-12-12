package com.ll.medium.member.controller;

import com.ll.medium.member.entity.Member;
import com.ll.medium.member.service.MemberService;
import com.ll.medium.member.util.MemberJoinForm;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/member")
@RequiredArgsConstructor
@Controller
public class MemberController {
    private final MemberService memberService;

    // == 회원가입 폼 ==
    @GetMapping("/join")
    public String joinForm(MemberJoinForm memberJoinForm) {
        return "/member/joinForm";
    }

    // == 회원가입 ==
    @PostMapping("/join")
    public String join(@Valid MemberJoinForm memberJoinForm,
                       BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "/member/joinForm";
        }

        if (!memberJoinForm.getPassword().equals(memberJoinForm.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "passwordInCorrect", "패스워드가 일치하지 않습니다.");
            return "/member/joinForm";
        }

        try {
            memberService.join(
                    memberJoinForm.getNickname(),
                    memberJoinForm.getUsername(),
                    memberJoinForm.getPassword());

        } catch (DataIntegrityViolationException e) {
            bindingResult.reject("signupFailed", "이미 등록된 닉네임입니다.");
            return "/member/joinForm";

        } catch (Exception e) {
            bindingResult.reject("signupFailed", e.getMessage());
            return "/member/joinForm";
        }

        return "/member/login";
    }

    // == 로그인 폼 ==
    @GetMapping("/login")
    public String login() {
        return "/member/login";
    }

    // == 마이페이지 ==
    @GetMapping("/mypage")
    public String myPage(Principal principal,
                         Model model) {
        Member findOne = memberService.findByNickname(principal.getName());
        model.addAttribute("member", findOne);
        return "/member/myPage";
    }
}
