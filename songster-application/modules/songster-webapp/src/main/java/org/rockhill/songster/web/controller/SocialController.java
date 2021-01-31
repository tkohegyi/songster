package org.rockhill.songster.web.controller;

import com.google.gson.Gson;
import org.rockhill.songster.database.tables.Social;
import org.rockhill.songster.exception.SystemException;
import org.rockhill.songster.web.controller.helper.ControllerBase;
import org.rockhill.songster.web.json.CurrentUserInformationJson;
import org.rockhill.songster.web.json.DeleteEntityJson;
import org.rockhill.songster.web.json.TableDataInformationJson;
import org.rockhill.songster.web.provider.CurrentUserProvider;
import org.rockhill.songster.web.provider.SocialProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Controller for handling records/information of Social (Facebook and/or Google) users.
 */
@Controller
public class SocialController extends ControllerBase {

    private final Logger logger = LoggerFactory.getLogger(SocialController.class);

    @Autowired
    private CurrentUserProvider currentUserProvider;
    @Autowired
    private SocialProvider socialProvider;

    /**
     * Serves the social page.
     *
     * @return the name of the social jsp file
     */
    @GetMapping(value = "/songsterSecure/social")
    public String social(HttpSession httpSession) {
        if (!isAdministrator(currentUserProvider, httpSession)) {
            return REDIRECT_TO_HOME;
        }
        return "social";
    }


    /**
     * Gets the list of Socials.
     *
     * @return with the list of socials as a JSON response
     */
    @ResponseBody
    @GetMapping(value = "/songsterSecure/getSocialTable")
    public TableDataInformationJson getSocialTable(HttpSession httpSession) {
        TableDataInformationJson content = null;
        if (isAdministrator(currentUserProvider, httpSession)) {
            //can get the person table
            Object people = socialProvider.getSocialListAsObject(); // this says [{"id":372,"name" we need data in head
            content = new TableDataInformationJson(people);
        }
        return content;
    }

    /**
     * Gets a specific Social record.
     *
     * @return with the social as a JSON response
     */
    @ResponseBody
    @GetMapping(value = "/songsterSecure/getSocial/{id:.+}")
    public TableDataInformationJson getSocialById(HttpSession httpSession, @PathVariable("id") final String requestedId) {
        TableDataInformationJson content = null;
        if (isAdministrator(currentUserProvider, httpSession)) {
            //can get the person
            Long id = Long.valueOf(requestedId);
            Object social = socialProvider.getSocialAsObject(id);
            content = new TableDataInformationJson(social);
        }
        return content;
    }

    /**
     * Update an existing Social record.
     *
     * @param session is the actual HTTP session
     * @return list of hits as a JSON response
     */
    @ResponseBody
    @PostMapping(value = "/songsterSecure/updateSocial")
    public ResponseEntity<String> updateSocial(@RequestBody final String body, final HttpSession session) {
        String resultString;
        ResponseEntity<String> result;
        try {
            CurrentUserInformationJson currentUserInformationJson = currentUserProvider.getUserInformation(session);
            //check authorization: user must have right user type
            if (!currentUserInformationJson.isAdmin) {
                result = buildUnauthorizedActionBodyResult();
            } else {
                //authorization checked, ok
                Gson g = new Gson();
                Social p = g.fromJson(body, Social.class);
                Long updateInformation = socialProvider.updateSocial(p, currentUserInformationJson);
                if (updateInformation != null) {
                    resultString = "OK-" + updateInformation.toString();
                    result = buildResponseBodyResult(JSON_RESPONSE_UPDATE, resultString, HttpStatus.CREATED);
                } else {
                    resultString = "Cannot update the Social, please check the values and retry.";
                    result = buildResponseBodyResult(JSON_RESPONSE_UPDATE, resultString, HttpStatus.BAD_REQUEST);
                    logger.info("Cannot update the Social with ID: {}", p.getId());
                }
            }
        } catch (SystemException e) {
            resultString = e.getMessage();
            result = buildResponseBodyResult(JSON_RESPONSE_UPDATE, resultString, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            resultString = "Cannot update the Social, please contact to maintainers.";
            result = buildResponseBodyResult(JSON_RESPONSE_UPDATE, resultString, HttpStatus.BAD_REQUEST);
            logger.warn("Error happened at Social, pls contact to maintainers", e);
        }
        return result;
    }

    /**
     * Gets log history of a specific Social record.
     *
     * @return with the social history as a JSON response
     */
    @ResponseBody
    @GetMapping(value = "/songsterSecure/getSocialHistory/{id:.+}")
    public TableDataInformationJson getSocialHistoryById(HttpSession httpSession, @PathVariable("id") final String requestedId) {
        TableDataInformationJson content = null;
        if (isAdministrator(currentUserProvider, httpSession)) {
            //can get the social history
            Long id = Long.valueOf(requestedId);
            Object socialHistory = socialProvider.getSocialHistoryAsObject(id);
            content = new TableDataInformationJson(socialHistory);
        }
        return content;
    }

    /**
     * Delete an existing Social record.
     *
     * @param session is the actual HTTP session
     * @return list of hits as a JSON response
     */
    @ResponseBody
    @PostMapping(value = "/songsterSecure/deleteSocial")
    public ResponseEntity<String> deletePerson(@RequestBody final String body, final HttpSession session) {
        String resultString;
        ResponseEntity<String> result;
        try {
            CurrentUserInformationJson currentUserInformationJson = currentUserProvider.getUserInformation(session);
            //check authorization
            if (!currentUserInformationJson.isAdmin) {
                result = buildUnauthorizedActionBodyResult();
            } else {
                //authorization checked, ok
                Gson g = new Gson();
                DeleteEntityJson p = g.fromJson(body, DeleteEntityJson.class);
                Long updatedObjectId = socialProvider.deleteSocial(p);
                if (updatedObjectId != null) {
                    resultString = "OK";
                    result = buildResponseBodyResult(JSON_RESPONSE_DELETE, resultString, HttpStatus.CREATED);
                } else {
                    resultString = "Cannot delete Social, please check and retry.";
                    result = buildResponseBodyResult(JSON_RESPONSE_DELETE, resultString, HttpStatus.BAD_REQUEST);
                    logger.info("Cannot delete Link - data issue.");
                }
            }
        } catch (SystemException e) {
            resultString = e.getMessage();
            result = buildResponseBodyResult(JSON_RESPONSE_DELETE, resultString, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            resultString = "Cannot delete Social, pls contact to maintainers.";
            result = buildResponseBodyResult(JSON_RESPONSE_DELETE, resultString, HttpStatus.BAD_REQUEST);
            logger.warn("Error happened at delete Social call", e);
        }
        return result;
    }

}
