package magyar.website.dalos.database.tables.help;

import magyar.website.dalos.exception.DatabaseHandlingException;

/**
 * Enum to represent the used Social Service of a Social record.
 */
public enum SocialServiceTypes {
    GITHUB("github", 1),
    GOOGLE("google", 2),
    FACEBOOK("facebook", 3),
    SELF_SERVICE("self_service", 4);

    private final String translatedText;
    private final Integer typeValue;

    SocialServiceTypes(String translatedText, Integer typeValue) {
        this.translatedText = translatedText;
        this.typeValue = typeValue;
    }

    /**
     * Get Social Service as a string.
     *
     * @param typeValue is the Id based identified of the enum.
     * @return with the string representation of the enum identified by the typeValue
     * @throws DatabaseHandlingException in case the give Id is invalid
     */
    public static String getTranslatedString(Integer typeValue) {
        for (SocialServiceTypes socialStatusTypes : SocialServiceTypes.values()) {
            if (socialStatusTypes.typeValue.equals(typeValue)) {
                return socialStatusTypes.getTranslatedText();
            }
        }
        throw new DatabaseHandlingException("Incorrect usage of data -> SocialServiceTypes type:" + typeValue.toString() + " was requested.");
    }

    /**
     * Gets the enum specified by its Id.
     *
     * @param id is the id that identified the Social ytpe enum
     * @return with the enum identified by the specified Id
     * @throws DatabaseHandlingException in case the specified Id is invalid
     */
    public static SocialServiceTypes getTypeFromId(Integer id) {
        for (SocialServiceTypes socialStatusTypes : SocialServiceTypes.values()) {
            if (socialStatusTypes.typeValue.equals(id)) {
                return socialStatusTypes;
            }
        }
        throw new DatabaseHandlingException("Incorrect usage of data -> SocialServiceTypes id:" + id.toString() + " was requested.");
    }

    // helper functions

    public Integer getTypeValue() {
        return typeValue;
    }

    public String getTranslatedText() {
        return translatedText;
    }

}
