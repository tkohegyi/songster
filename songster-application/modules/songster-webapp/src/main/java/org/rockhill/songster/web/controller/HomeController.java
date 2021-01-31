package org.rockhill.songster.web.controller;

import org.eclipse.jetty.server.Request;
import org.rockhill.songster.web.controller.helper.ControllerBase;
import org.rockhill.songster.web.json.CurrentUserInformationJson;
import org.rockhill.songster.web.provider.CurrentUserProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Controller for handling requests for the application home page.
 *
 * @author Tamas Kohegyi
 */
@Controller
public class HomeController extends ControllerBase {
    private static final String JSON_LOGGED_IN_USER_INFO = "loggedInUserInfo";
    private final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private CurrentUserProvider currentUserProvider;

    /**
     * Serves requests which arrive to home and sends back the home page.
     *
     * @return the name of the jsp to display as result
     */
    @GetMapping(value = "/")
    public String pseudoHome() {
        return REDIRECT_TO_HOME;
    }

    /**
     * Serving the Home page request.
     *
     * @return with proper content
     */
    @GetMapping(value = "/songster/")
    public String realHome(HttpSession httpSession) {
        CurrentUserInformationJson currentUserInformationJson = currentUserProvider.getUserInformation(httpSession);
        if (currentUserInformationJson.isLoggedIn) {
            return "redirect:/songsterSecure/songs";
        }
        //not logged in!
        return "redirect:/songster/login";
    }

    /**
     * Serving "favicon.ico" request.
     *
     * @return with proper content
     */
    @GetMapping(value = "/favicon.ico")
    public String favicon() {
        return "redirect:/resources/img/favicon.ico";
    }

    /**
     * Serving "robots.txt" request.
     *
     * @return with the proper content
     */
    @GetMapping(value = "/robots.txt")
    public String robots() {
        return "redirect:/resources/robots.txt";
    }

    /**
     * Serving the ".well-known/security.txt" request.
     *
     * @return with the proper content
     */
    @GetMapping(value = "/.well-known/security.txt")
    public String securityText() {
        return "redirect:/resources/security.txt";
    }

    /**
     * Serving "sitemap.xml" request.
     *
     * @return with the proper content
     */
    @GetMapping(value = "/sitemap.xml")
    public String siteMapXml() {
        return "redirect:/resources/sitemap.xml";
    }

    /**
     * Serving "sitemap.txt" request.
     *
     * @return with the proper content
     */
    @GetMapping(value = "/sitemap.txt")
    public String siteMapText() {
        return "redirect:/resources/sitemap.txt";
    }

    /**
     * Grace handling of E404 (File not found) issues.
     *
     * @param httpServletRequest is the request
     * @return with proper content
     */
    @RequestMapping(value = "/songster/e404", method = {RequestMethod.GET, RequestMethod.POST})
    public String e404(HttpServletRequest httpServletRequest) {
        String originalUri = "unknown";
        if (httpServletRequest instanceof Request) {
            originalUri = ((Request) httpServletRequest).getOriginalURI();
        }
        logger.info("E404 caused by: {}, method: {}, URI: {}",
                httpServletRequest.getRemoteHost(), httpServletRequest.getMethod(), originalUri);
        return "E404";
    }

    /**
     * Grace handling of E500 (Internal Server Error) issues.
     *
     * @param httpServletRequest is the request
     * @return with proper content
     */
    @RequestMapping(value = "/songster/e500", method = {RequestMethod.GET, RequestMethod.POST})
    public String e500(HttpServletRequest httpServletRequest) {
        String originalUri = "unknown";
        if (httpServletRequest instanceof Request) {
            originalUri = ((Request) httpServletRequest).getOriginalURI();
        }
        logger.info("E500 caused by: {}, method: {}, URI: {}",
                httpServletRequest.getRemoteHost(), httpServletRequest.getMethod(), originalUri);
        return "E404";
    }

    /**
     * Serves information about the logged in user.
     *
     * @param httpSession         is the session of the user
     * @return with proper content
     */
    @ResponseBody
    @GetMapping(value = "/songster/getLoggedInUserInfo")
    public ResponseEntity<String> getLoggedInUserInfo(HttpSession httpSession) {
        ResponseEntity<String> result;
        CurrentUserInformationJson currentUserInformationJson = currentUserProvider.getUserInformation(httpSession);
        result = buildResponseBodyResult(JSON_LOGGED_IN_USER_INFO, currentUserInformationJson, HttpStatus.OK);
        return result;
    }

}
