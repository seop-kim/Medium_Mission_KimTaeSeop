package com.ll.medium.comment.util;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CommentForm {
    @NotEmpty
    private String content;
}
