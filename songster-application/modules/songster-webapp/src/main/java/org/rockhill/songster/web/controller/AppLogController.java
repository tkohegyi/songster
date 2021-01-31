package org.rockhill.songster.web.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.rockhill.songster.web.controller.helper.ControllerBase;
import org.rockhill.songster.web.provider.CurrentUserProvider;
import org.rockhill.songster.web.provider.LogFileProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller for accessing the application log files.
 */
@Controller
public class AppLogController extends ControllerBase {

    private static final String JSON_NAME = "files";
    private static final String CONTENT_DISPOSITION = "Content-Disposition";
    private static final String ATTACHMENT_TEMPLATE = "attachment; filename=%s";
    private static final String JSON_APP_INFO = "songsterApplication";
    private final RequestMappingHandlerMapping handlerMapping;
    private final Logger logger = LoggerFactory.getLogger(AppLogController.class);

    @Autowired
    private LogFileProvider logFileProvider;
    @Autowired
    private CurrentUserProvider currentUserProvider;

    /**
     * Constructor of the controller for accessing the application log files.
     *
     * @param handlerMapping is the map of the handlers used by the running web server
     */
    @Autowired
    public AppLogController(RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    /**
     * Serves the applog page.
     *
     * @return the name of the applog jsp file
     */
    @GetMapping(value = "/songsterSecure/applog")
    public String appLog(HttpSession httpSession, HttpServletResponse httpServletResponse) {
        if (!isAdministrator(currentUserProvider, httpSession)) {
            return REDIRECT_TO_HOME;
        }
        return "applog";
    }

    /**
     * Gets the list of log files.
     *
     * @return with the list of log files as a JSON response
     */
    @ResponseBody
    @GetMapping(value = "/songsterSecure/logs")
    public Map<String, Collection<String>> getLogFiles(HttpSession httpSession) {
        Map<String, Collection<String>> jsonResponse = new HashMap<>();
        if (isAdministrator(currentUserProvider, httpSession)) {
            jsonResponse.put(JSON_NAME, logFileProvider.getLogFileNames());
        }
        return jsonResponse;
    }

    /**
     * Gets the content of the log file.
     *
     * @param fileName  the name of the log file
     * @param source    true if the content should be written directly, false for attachment
     * @param userAgent the User-Agent of the request header
     * @return the content of the log file
     */
    @GetMapping(value = "/songsterSecure/logs/{fileName:.+}")
    public ResponseEntity<String> getLogFileContent(HttpSession httpSession,
                                                    @PathVariable("fileName") final String fileName,
                                                    @RequestParam(value = "source", defaultValue = "false") final boolean source,
                                                    @RequestHeader(value = "User-Agent", defaultValue = "") final String userAgent) {
        ResponseEntity<String> responseEntity;
        String body = UNAUTHORIZED_ACTION;
        if (isAdministrator(currentUserProvider, httpSession)) {
            body = logFileProvider.getLogContent(fileName);
            body = convertLineBreaksIfOnWindows(body, userAgent);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        if (!source) {
            headers.set(CONTENT_DISPOSITION, String.format(ATTACHMENT_TEMPLATE, fileName));
        }
        responseEntity = new ResponseEntity<>(body, headers, HttpStatus.OK);
        return responseEntity;
    }

    private String convertLineBreaksIfOnWindows(final String body, final String userAgent) {
        String result = body;
        if (userIsOnWindows(userAgent)) {
            result = body.replace("\r", "").replace("\n", "\r\n");
        }
        return result;
    }

    private boolean userIsOnWindows(final String userAgent) {
        return userAgent.toLowerCase().contains("windows");
    }

    /**
     * Gets the list of endpoints offered and served by the server.
     *
     * @return with the result page
     */
    @ResponseBody
    @GetMapping(value = "/songster/endpointdoc")
    public Map<String, Collection<String>> showEndPoints(HttpSession httpSession) {
        Map<RequestMappingInfo, HandlerMethod> methods = this.handlerMapping.getHandlerMethods();
        Map<String, Collection<String>> jsonResponse = new HashMap<>();

        if (isAdministrator(currentUserProvider, httpSession)) {  //visible only for admins
            for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : methods.entrySet()) {
                Collection<String> collection = new ArrayList<>();
                collection.add(entry.getValue().toString());
                jsonResponse.put(entry.getKey().toString(), collection);
            }
        }
        return jsonResponse;
    }

    /**
     * Get information about the server itself.
     *
     * @return with the response page
     */
    @ResponseBody
    @GetMapping(value = "/songsterSecure/getAppServerInfo")
    public Map<String, Collection<String>> getAppServerInfo() {

        Map<String, Collection<String>> jsonResponse = new HashMap<>();
        Collection<String> jsonString = new ArrayList<>();

        JsonObject jsonObject = new JsonObject();
        Gson gson = new Gson();
        InetAddress ip;
        String hostname;
        try {
            ip = InetAddress.getLocalHost(); //ip address
            hostname = ip.getHostName();
            jsonObject.add("ip", gson.toJsonTree(ip.toString()));
            jsonObject.add("hostname", gson.toJsonTree(hostname));
        } catch (UnknownHostException e) {
            logger.info("Login page - cannot detect ip/hostname.");
        }
        String json = gson.toJson(jsonObject);
        jsonString.add(json);
        jsonResponse.put(JSON_APP_INFO, jsonString);
        return jsonResponse;
    }
}
