package magyar.website.dalos.web.oauth2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OnLoginSuccess extends SavedRequestAwareAuthenticationSuccessHandler {
    static private final Logger logger = LoggerFactory.getLogger(OnLoginSuccess.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        DefaultOAuth2User userDetails = (DefaultOAuth2User) authentication.getPrincipal();
        String method = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
        String id = userDetails.getName();
        String name = userDetails.getAttribute("name");
        String email = userDetails.getAttribute("email");
        String picture = switch (method) {
            case "google" -> userDetails.getAttribute("picture");
            case "facebook" -> "empty avatar";
            case "github" -> userDetails.getAttribute("avatar_url");
            default -> throw new IllegalStateException("Unexpected value: " + method);
        };

        logger.info("The user/id: {}/{} has logged in by using: {}.", name, id, method);
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
