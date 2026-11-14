package util;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * Represents a day of the week on which a health professional can work.
 *
 * <p>An enum is used because the set of weekdays is fixed and known in
 * advance. Each constant also carries a readable label for console
 * output.</p>
 */
public enum Weekday {
    /** Monday. */
    MONDAY("Monday"),
    /** Tuesday. */
    TUESDAY("Tuesday"),
    /** Wednesday. */
    WEDNESDAY("Wednesday"),
    /** Thursday. */
    THURSDAY("Thursday"),
    /** Friday. */
    FRIDAY("Friday"),
    /** Saturday. */
    SATURDAY("Saturday"),
    /** Sunday. */
    SUNDAY("Sunday");

    private final String displayName;

    /**
     * Creates a weekday with its display label.
     *
     * @param displayName the label shown to users
     */
    Weekday(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the readable name of this weekday.
     *
     * @return the display label, such as {@code "Monday"}
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns the current day according to the system clock.
     *
     * @return today's weekday
     */
    public static Weekday today() {
        return fromDayOfWeek(LocalDate.now().getDayOfWeek());
    }

    /**
     * Converts a {@link DayOfWeek} into the matching {@code Weekday}.
     *
     * <p>The constants in this enum are declared in the same order as the ISO
     * day numbering used by {@link DayOfWeek}: Monday is 1 and Sunday is 7.</p>
     *
     * @param dayOfWeek the Java day-of-week value
     * @return the matching weekday
     * @throws IllegalArgumentException if {@code dayOfWeek} is {@code null}
     */
    public static Weekday fromDayOfWeek(DayOfWeek dayOfWeek) {
        if (dayOfWeek == null) {
            throw new IllegalArgumentException("Day of week must not be null.");
        }
        return values()[dayOfWeek.getValue() - 1];
    }

    /**
     * Returns the readable label instead of the enum constant name.
     *
     * @return the display label
     */
    @Override
    public String toString() {
        return displayName;
    }
}
