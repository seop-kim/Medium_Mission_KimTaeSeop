package com.ll.medium.home.home;

import com.ll.medium.member.member.entity.Member;
import com.ll.medium.member.member.service.MemberService;
import com.ll.medium.post.post.service.PostService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/adm")
@RequiredArgsConstructor
public class AdmHomeController {
    private final MemberService memberService;
    private final PostService postService;

    @GetMapping("")
    public String showMain() {
        return "domain/home/home/adm/main";
    }

    @GetMapping("/home/about")
    public String showAbout() {
        return "domain/home/home/adm/about";
    }

    @GetMapping("/home/memberManage")
    public String showMemberManage(Model model) {
        List<Member> findAll = memberService.findAll();
        model.addAttribute("members", findAll);
        return "domain/home/home/adm/memberManage";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/home/member/membership")
    public String memberMembershipUpdate(@RequestParam("id") long id,
                                         @RequestParam("membership")boolean membership) {
        Member member = memberService.findById(id).get();
        memberService.updateMembership(member, membership);
        return "redirect:/adm/home/memberManage";
    }
}