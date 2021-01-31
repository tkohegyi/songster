package org.rockhill.songster.web.controller;

import org.rockhill.songster.web.controller.helper.ControllerBase;
import org.rockhill.songster.web.json.TableDataInformationJson;
import org.rockhill.songster.web.provider.AuditProvider;
import org.rockhill.songster.web.provider.CurrentUserProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Controller for handling requests about the audit records.
 *
 * @author Tamas Kohegyi
 */
@Controller
public class AuditController extends ControllerBase {
    private final Logger logger = LoggerFactory.getLogger(AuditController.class);
    @Autowired
    private CurrentUserProvider currentUserProvider;
    @Autowired
    private AuditProvider auditProvider;

    /**
     * Serves administrator request about audit records.
     *
     * @return the name of the jsp to display the result
     */
    @GetMapping(value = "/songsterSecure/audit")
    public String audit(HttpSession httpSession) {
        if (isAdministrator(currentUserProvider, httpSession)) { //only admins
            return "audit";
        }
        return REDIRECT_TO_HOME; //not admin -> go back to basic home page
    }

    /**
     * Get list of audit records of the last N (=requestedDays) days.
     *
     * @param httpSession   identifies the user
     * @param requestedDays are the last N days for the query, must be > 0
     * @return with proper content
     */
    @ResponseBody
    @GetMapping(value = "/songsterSecure/getAuditTrailByDays/{days:.+}")
    public TableDataInformationJson getAuditTrailByDays(HttpSession httpSession, @PathVariable("days") final String requestedDays) {
        TableDataInformationJson content = null;
        if (isAdministrator(currentUserProvider, httpSession)) {
            //has right to collect and see information
            try {
                Long days = Long.parseLong(requestedDays);
                Object information = auditProvider.getAuditTrailOfLastDays(days);
                content = new TableDataInformationJson(information);
            } catch (NumberFormatException e) {
                logger.warn("Rouge request to getAuditTrailByDays endpoint with bad days parameter.");
            }
        }
        return content;
    }

}
