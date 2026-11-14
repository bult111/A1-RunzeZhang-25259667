package model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents one fixed 30-minute appointment time slot, for example
 * {@code 09:00 - 09:30}.
 *
 * <p>The clinic uses the same slot length for every appointment, so the end
 * time is calculated from the start time. Storing {@link LocalTime} values
 * lets slots be compared and sorted correctly.</p>
 */
public class TimeSlot implements Comparable<TimeSlot> {

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    private static final int SLOT_DURATION_MINUTES = 30;

    private final LocalTime startTime;
    private final LocalTime endTime;

    /**
     * Creates a 30-minute time slot that starts at the supplied time.
     *
     * @param startTime the start time of the slot
     * @throws IllegalArgumentException if the start time is missing, contains
     *                                  seconds or nanoseconds, or does not
     *                                  leave room for a 30-minute slot
     */
    public TimeSlot(LocalTime startTime) {
        if (startTime == null) {
            throw new IllegalArgumentException("Time slot start time must not be null.");
        }
        if (startTime.getSecond() != 0 || startTime.getNano() != 0) {
            throw new IllegalArgumentException("Time slots must start on a whole minute.");
        }

        LocalTime calculatedEndTime = startTime.plusMinutes(SLOT_DURATION_MINUTES);
        if (!calculatedEndTime.isAfter(startTime)) {
            throw new IllegalArgumentException("A time slot must end after it starts.");
        }

        this.startTime = startTime;
        this.endTime = calculatedEndTime;
    }

    /**
     * Returns the start time of this slot.
     *
     * @return the start time
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Returns the end time of this slot.
     *
     * @return the end time
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * Returns a readable label for the slot, such as
     * {@code 09:00 - 09:30}.
     *
     * @return the formatted slot label
     */
    public String getLabel() {
        return startTime.format(TIME_FORMAT) + " - " + endTime.format(TIME_FORMAT);
    }

    /**
     * Compares slots by start time and then by end time.
     *
     * @param other the slot to compare with
     * @return a negative number, zero, or a positive number if this slot is
     *         earlier than, equal to, or later than the other slot
     * @throws NullPointerException if {@code other} is {@code null}
     */
    @Override
    public int compareTo(TimeSlot other) {
        if (other == null) {
            throw new NullPointerException("Cannot compare a time slot with null.");
        }

        int result = startTime.compareTo(other.startTime);
        if (result != 0) {
            return result;
        }
        return endTime.compareTo(other.endTime);
    }

    /**
     * Compares this slot with another object by start and end time.
     *
     * @param other the object to compare with
     * @return {@code true} when both slots cover exactly the same interval
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TimeSlot)) {
            return false;
        }

        TimeSlot slot = (TimeSlot) other;
        return startTime.equals(slot.startTime) && endTime.equals(slot.endTime);
    }

    /**
     * Returns a hash code consistent with {@link #equals(Object)}.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return 31 * startTime.hashCode() + endTime.hashCode();
    }

    /**
     * Returns the readable slot label.
     *
     * @return the slot label
     */
    @Override
    public String toString() {
        return getLabel();
    }
}
