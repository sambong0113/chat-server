package com.ys.chatserver.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiResponse {

    public static final String SUCCESS_MESSAGE = "SUCCESS";
    public static final String NOT_FOUND_MESSAGE = "NOT FOUND";
    public static final String FAILED_MESSAGE = "서버에서 오류가 발생하였습니다.";
    public static final String INVALID_ACCESS_TOKEN = "Invalid access token.";
    public static final String INVALID_REFRESH_TOKEN = "Invalid refresh token.";
    public static final String NOT_EXPIRED_TOKEN_YET = "Not expired token yet.";
}
