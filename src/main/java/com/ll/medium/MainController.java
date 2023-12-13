package com.ll.medium;

import com.ll.medium.post.entity.Post;
import com.ll.medium.post.service.PostService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Slf4j
@Controller
public class MainController {
    private final PostService postService;

    @GetMapping("/")
    public String home(Model model) {
        List<Post> posts = postService.getHomeList();
        model.addAttribute("posts", posts);
        return "/common/home";
    }
}
