package org.rockhill.songster.web.provider.helper;

import org.rockhill.songster.database.business.BusinessWithAuditTrail;
import org.rockhill.songster.database.tables.AuditTrail;

import java.util.List;

/**
 * Base class of Providers.
 */
public class ProviderBase {

    /**
     * Detects if the two long is different. Any of hem can be null too.
     * If both of them is null, that means they are the same.
     *
     * @param oldLongValue is one value
     * @param newLongValue is the other value
     * @return true if different, otherwise false
     */
    protected boolean isLongChanged(final Long oldLongValue, final Long newLongValue) {
        boolean changed = false;
        if ((!((oldLongValue == null) && (newLongValue == null))) && //if both null, it was not changed
                (oldLongValue == null || !oldLongValue.equals(newLongValue))) { //if old is null then the new is not ...
            changed = true;
        }
        return changed;
    }

    /**
     * Converts a long to a string, defaulting to "N/A" in case the long is null.
     *
     * @param value of the long
     * @return with its string
     */
    protected String prepareAuditValueString(Long value) {
        String stringValue;
        if (value != null) {
            stringValue = value.toString();
        } else {
            stringValue = "N/A";
        }
        return stringValue;
    }

    /**
     * Gets the list of audit events associated to a specific object Id.
     *
     * @param businessWithAuditTrail is the business class to work with audit record
     * @param id                     identifies the record we are interested about its history
     * @return with the list of audit trail that belong to the specified item, as object
     */
    protected Object getEntityHistoryAsObject(BusinessWithAuditTrail businessWithAuditTrail, Long id) {
        List<AuditTrail> auditTrailOfObject;
        auditTrailOfObject = businessWithAuditTrail.getAuditTrailOfObject(id);
        return auditTrailOfObject;
    }

}
