package org.rockhill.songster.web.json;

import org.rockhill.songster.helper.JsonField;

/**
 * Json structure to be used to delete an entity.
 * Since every database record has its own unique id, specifying the id is enough.
 */
public class DeleteEntityJson {
    @JsonField
    public String entityId;
}
