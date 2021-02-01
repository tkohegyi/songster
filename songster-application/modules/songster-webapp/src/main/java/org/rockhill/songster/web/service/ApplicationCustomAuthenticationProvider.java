package org.rockhill.songster.web.service;

import org.rockhill.songster.database.tables.Social;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom Authentication Provider for the application.
 */
@Component
public class ApplicationCustomAuthenticationProvider implements AuthenticationProvider {

    /**
     * Do the custom authentication for the application.
     *
     * @param authentication is the object the authentication will be performed
     * @return with either the authenticated object or null
     * @throws AuthenticationException as the implemented method expects it
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication validAuthentication = null;
        if ((authentication != null) && (authentication.getPrincipal() instanceof GoogleUser)) {
            GoogleUser googleUser = (GoogleUser) authentication.getPrincipal();
            Social social = googleUser.getSocial();
            if (social != null) {
                //authenticated !
                List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
                validAuthentication = new PreAuthenticatedAuthenticationToken(googleUser, authentication.getCredentials(), grantedAuthorities);
                //google login success
            }
        }
        if ((authentication != null) && (authentication.getPrincipal() instanceof FacebookUser)) {
            FacebookUser facebookUser = (FacebookUser) authentication.getPrincipal();
            Social social = facebookUser.getSocial();
            if (social != null) {
                //authenticated !
                List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
                validAuthentication = new PreAuthenticatedAuthenticationToken(facebookUser, authentication.getCredentials(), grantedAuthorities);
                //facebook login success
            }
        }
        return validAuthentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(PreAuthenticatedAuthenticationToken.class);
    }
}