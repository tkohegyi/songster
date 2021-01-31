package org.rockhill.songster.web.json;

import org.rockhill.songster.helper.JsonField;

/**
 * Json structure that is used when a message has been sent to the administrator.
 */
public class MessageToCoordinatorJson {
    @JsonField
    public String info;
    @JsonField
    public String text;
    @JsonField
    public String captcha;
}

