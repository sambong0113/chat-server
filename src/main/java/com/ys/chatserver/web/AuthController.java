package com.ys.chatserver.web;

import com.ys.chatserver.common.ApiResponse;
import com.ys.chatserver.config.properties.AppProperties;
import com.ys.chatserver.config.token.AuthToken;
import com.ys.chatserver.config.token.AuthTokenProvider;
import com.ys.chatserver.domain.auth.AuthReqModel;
import com.ys.chatserver.domain.user.Role;
import com.ys.chatserver.domain.user.UserPrincipal;
import com.ys.chatserver.domain.user.UserRefreshToken;
import com.ys.chatserver.domain.user.UserRefreshTokenRepository;
import com.ys.chatserver.utils.CookieUtil;
import com.ys.chatserver.utils.HeaderUtil;
import com.ys.chatserver.web.dto.TokenResponseDto;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AppProperties appProperties;
    private final AuthTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserRefreshTokenRepository userRefreshTokenRepository;

    private static final long THREE_DAYS_MSEC = 259200000;
    private static final String REFRESH_TOKEN = "refresh_token";

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody AuthReqModel authReqModel
    ) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authReqModel.getId(),
                        authReqModel.getPassword()
                )
        );

        String userId = authReqModel.getId();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Date now = new Date();
        AuthToken accessToken = tokenProvider.createAuthToken(
                userId,
                ((UserPrincipal) authentication.getPrincipal()).getRole().getCode(),
                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );

        long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();
        AuthToken refreshToken = tokenProvider.createAuthToken(
                appProperties.getAuth().getTokenSecret(),
                new Date(now.getTime() + refreshTokenExpiry)
        );

        // userId refresh token ?????? DB ??????
        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserId(userId);
        if (userRefreshToken == null) {
            // ?????? ?????? ?????? ??????
            userRefreshToken = new UserRefreshToken(userId, refreshToken.getToken());
            userRefreshTokenRepository.saveAndFlush(userRefreshToken);
        } else {
            // DB??? refresh ?????? ????????????
            userRefreshToken.setRefreshToken(refreshToken.getToken());
        }

        int cookieMaxAge = (int) refreshTokenExpiry / 60;
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
        CookieUtil.addCookie(response, REFRESH_TOKEN, refreshToken.getToken(), cookieMaxAge);


        return ResponseEntity.ok()
                .body(new TokenResponseDto(accessToken.getToken()));
    }

    @GetMapping("/refresh")
    public ResponseEntity<TokenResponseDto> refreshToken(HttpServletRequest request, HttpServletResponse response) {

        String accessToken = HeaderUtil.getAccessToken(request);
        AuthToken authToken = tokenProvider.convertAuthToken(accessToken);
        if (!authToken.validate()) {
           return ResponseEntity.status(500)
                   .header(ApiResponse.FAILED_MESSAGE, ApiResponse.INVALID_REFRESH_TOKEN)
                   .body(null);
        }

        Claims claims = authToken.getExpiredTokenClaims();
        if (claims == null) {
           return ResponseEntity.status(500)
                   .header(ApiResponse.FAILED_MESSAGE, ApiResponse.NOT_EXPIRED_TOKEN_YET)
                   .body(null);
        }

        String userId = claims.getSubject();
        Role role = Role.of(claims.get("role", String.class));

        // refresh token
        String refreshToken = CookieUtil.getCookie(request, REFRESH_TOKEN)
                .map(Cookie::getValue)
                .orElse((null));
        AuthToken authRefreshToken = tokenProvider.convertAuthToken(refreshToken);

        if (authRefreshToken.validate()) {
           return ResponseEntity.status(500)
                   .header(ApiResponse.FAILED_MESSAGE, ApiResponse.INVALID_REFRESH_TOKEN)
                   .body(null);
        }

        // userId refresh token ?????? DB ??????
        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserIdAndRefreshToken(userId, refreshToken);
        if (userRefreshToken == null) {
           return ResponseEntity.status(500)
                   .header(ApiResponse.FAILED_MESSAGE, ApiResponse.INVALID_REFRESH_TOKEN)
                   .body(null);
        }

        Date now = new Date();

        AuthToken newAccessToken = tokenProvider.createAuthToken(
                userId,
                role.getCode(),
                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );

        long validTime = authRefreshToken.getTokenClaims().getExpiration().getTime() - now.getTime();

        // refresh ?????? ????????? 3??? ????????? ?????? ??????, refresh ?????? ??????
        if (validTime <= THREE_DAYS_MSEC) {
            // refresh ?????? ??????
            long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();

            authRefreshToken = tokenProvider.createAuthToken(
                    appProperties.getAuth().getTokenSecret(),
                    new Date(now.getTime() + refreshTokenExpiry)
            );

            // DB??? refresh ?????? ????????????
            userRefreshToken.setRefreshToken(authRefreshToken.getToken());

            int cookieMaxAge = (int) refreshTokenExpiry / 60;
            CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
            CookieUtil.addCookie(response, REFRESH_TOKEN, authRefreshToken.getToken(), cookieMaxAge);
        }

        return ResponseEntity.ok()
                .body(new TokenResponseDto(newAccessToken.getToken()));
    }
}
