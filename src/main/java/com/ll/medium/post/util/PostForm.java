package com.ll.medium.post.util;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostForm {
    @NotEmpty(message = "제목은 필수 항목입니다.")
    @Size(max = 200)
    private String title;

    @NotEmpty(message = "내용은 필수 항목입니다.")
    private String content;

    private boolean isPublish;
}
