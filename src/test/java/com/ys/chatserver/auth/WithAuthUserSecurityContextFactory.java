package com.ys.chatserver.auth;

import com.ys.chatserver.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithAuthUserSecurityContextFactory implements WithSecurityContextFactory<WithAuthUser> {

    @Autowired
    AuthenticationManager authenticationManager;

    @Override
    public SecurityContext createSecurityContext(WithAuthUser annotation) {
//        String email = annotation.email();
        String role = annotation.role();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        "email",
                        ""
                )
        );

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return context;
    }
}
