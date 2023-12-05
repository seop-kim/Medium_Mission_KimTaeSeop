package com.ll.medium.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/post")
@RequiredArgsConstructor
@Controller
public class PostController {

    @GetMapping("/home")
    @ResponseBody
    public String home() {
        return "웹 홈";
    }

    @GetMapping("/")
    @ResponseBody
    public String list() {
        return "게시물 리스트가 있을 곳 입니다.";
    }

    @GetMapping("/myList")
    @ResponseBody
    public String myList() {
        return "내 글 리스트 페이지입니다.";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public String detail(@PathVariable("id") Long id) {
        return "글 상세내용입니다.";
    }

    @GetMapping("/write")
    @ResponseBody
    public String writeForm() {
        return "게시글 작성 폼 입니다.";
    }

    @PostMapping("/write")
    @ResponseBody
    public String write() {
        return "게시글 작성 기능입니다.";
    }

    @GetMapping("/{id}/modify")
    @ResponseBody
    public String modifyForm(@PathVariable("id") Long id) {
        return "게시글 수정 폼 입니다.";
    }

    @PutMapping("/{id}/modify")
    @ResponseBody
    public String modify(@PathVariable("id") Long id) {
        return "redirect:/게시글 수정 기능입니다.";
    }

    @DeleteMapping("/{id}/delete")
    @ResponseBody
    public String delete(@PathVariable("id") Long id) {
        return "redirect:/게시글 삭제 기능입니다.";
    }
}
