package org.rockhill.songster.web.controller.helper;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.rockhill.songster.web.json.CurrentUserInformationJson;
import org.rockhill.songster.web.provider.CurrentUserProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpSession;

/**
 * Base class of every controller.
 */
public class ControllerBase {

    public static final String JSON_RESPONSE_UPDATE = "entityUpdate";
    public static final String JSON_RESPONSE_DELETE = "entityDelete";
    protected static final String REDIRECT_TO_HOME = "redirect:/songster/";
    protected static final String UNAUTHORIZED_ACTION = "Unauthorized action.";

    /**
     * Checks if the current user is administrator or not.
     *
     * @return true when the user is administrator.
     */
    public boolean isAdministrator(CurrentUserProvider currentUserProvider, HttpSession httpSession) {
        CurrentUserInformationJson currentUserInformationJson = currentUserProvider.getUserInformation(httpSession);
        return currentUserInformationJson.isAdmin;
    }

    /**
     * Checks if the current user is a privileged user or not.
     *
     * @return true if the user is a privileged user
     */
    public boolean isPrivilegedUser(CurrentUserProvider currentUserProvider, HttpSession httpSession) {
        CurrentUserInformationJson currentUserInformationJson = currentUserProvider.getUserInformation(httpSession);
        return currentUserInformationJson.isPrivilegedUser;
    }

    /**
     * Checks if the current user is a registered user or not.
     *
     * @return true if the user is a registered user
     */
    public boolean isRegisteredUser(CurrentUserProvider currentUserProvider, HttpSession httpSession) {
        CurrentUserInformationJson currentUserInformationJson = currentUserProvider.getUserInformation(httpSession);
        return currentUserInformationJson.isRegisteredUser;
    }

    /**
     * Prepares media type: json for a response.
     *
     * @return with the necessary headers
     */
    protected HttpHeaders setHeadersForJSON() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        responseHeaders.add("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
        responseHeaders.add("Pragma", "no-cache");
        return responseHeaders;
    }

    /**
     * Returns with a String version of a Json object.
     * Like:
     * { id : { the object } }
     *
     * @param id     is the element id
     * @param object is the object
     * @return with the string version of the Json object
     */
    protected String getJsonString(final String id, final Object object) {
        String json;
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(id, gson.toJsonTree(object));
        json = gson.toJson(jsonObject);
        return json;
    }

    /**
     * Builds a full prepared json answer.
     *
     * @param resultId     is the identifier of the request type
     * @param resultObject is the result object itself
     * @param httpStatus   is the result HTTP status value
     * @return with the ready-to-send response
     */
    protected ResponseEntity<String> buildResponseBodyResult(final String resultId, final Object resultObject, final HttpStatus httpStatus) {
        ResponseEntity<String> result;
        HttpHeaders responseHeaders = setHeadersForJSON();
        result = new ResponseEntity<>(getJsonString(resultId, resultObject), responseHeaders, httpStatus);
        return result;
    }

    /**
     * Builds a full prepared json response, that says that the request was unauthorized.
     *
     * @return with the ready-to-send response
     */
    protected ResponseEntity<String> buildUnauthorizedActionBodyResult() {
        ResponseEntity<String> result;
        result = buildResponseBodyResult(JSON_RESPONSE_UPDATE, UNAUTHORIZED_ACTION, HttpStatus.FORBIDDEN);
        return result;
    }

}
