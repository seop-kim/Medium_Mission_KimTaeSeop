package com.ll.medium;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String main() {
        // test로 인해 list로 전달
        return "redirect:/post/list";
    }
}
