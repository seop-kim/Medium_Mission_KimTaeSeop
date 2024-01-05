package com.ll.medium.global.exception;

import com.ll.medium.global.rsData.RsData;
import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException{
    private RsData<?> rsData;

    public GlobalException(String resultCode, String msg) {
        super(resultCode + " " + msg);
        this.rsData = RsData.of(resultCode, msg);
    }
}
