package com.ll.medium.member.util;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MemberJoinForm {
    @NotEmpty
    @Size(min = 1, max = 5)
    private String username;

    @NotEmpty
    @Size(min = 3, max = 20)
    private String nickname;

    @NotEmpty
    private String password;

    @NotEmpty
    private String passwordConfirm;
}
