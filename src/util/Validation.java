package util;

/**
 * Provides small validation helpers shared by the appointment system.
 *
 * <p>Only the checks needed by this assignment are included: required text,
 * numeric IDs, and simple blank-value detection.</p>
 */
public final class Validation {

    /**
     * Private constructor prevents this utility class from being instantiated.
     */
    private Validation() {
    }

    /**
     * Checks whether a string is {@code null}, empty, or contains only
     * whitespace.
     *
     * @param value the value to check
     * @return {@code true} when the value has no visible characters
     */
    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * Ensures that a required text value is present and returns it without
     * leading or trailing whitespace.
     *
     * @param value     the value to check
     * @param fieldName the name used in the error message
     * @return the trimmed value
     * @throws IllegalArgumentException if the value is blank
     */
    public static String requireNonBlank(String value, String fieldName) {
        if (isBlank(value)) {
            throw new IllegalArgumentException(fieldName + " must not be blank.");
        }
        return value.trim();
    }

    /**
     * Ensures that a value contains digits only and returns it without leading
     * or trailing whitespace.
     *
     * @param value     the value to check
     * @param fieldName the name used in the error message
     * @return the trimmed numeric value
     * @throws IllegalArgumentException if the value is blank or not numeric
     */
    public static String requireNumeric(String value, String fieldName) {
        String trimmedValue = requireNonBlank(value, fieldName);

        for (int index = 0; index < trimmedValue.length(); index++) {
            if (!Character.isDigit(trimmedValue.charAt(index))) {
                throw new IllegalArgumentException(fieldName + " must contain numbers only.");
            }
        }

        return trimmedValue;
    }
}
