package magyar.website.dalos.web.oauth2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OnLogoutSuccess extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {
    static private final Logger logger = LoggerFactory.getLogger(OnLogoutSuccess.class);

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {

        DefaultOAuth2User userDetails = (DefaultOAuth2User) authentication.getPrincipal();
        String method = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
        String id = userDetails.getName();
        String name = userDetails.getAttribute("name");

        logger.info("The user/id: {}/{} has logged out from: {}.", name, id, method);

        super.onLogoutSuccess(request, response, authentication);
    }
}
