package model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Defines the clinic's fixed daily appointment slots.
 *
 * <p>The standard clinic day runs from 08:00 to 16:00 in 30-minute slots.
 * The opening time, closing time, and slot length are private constants, so a
 * future change to the clinic day is made in one place and the rest of the
 * appointment system continues to use the same class.</p>
 */
public class ClinicSchedule {

    private static final LocalTime OPENING_TIME = LocalTime.of(8, 0);
    private static final LocalTime CLOSING_TIME = LocalTime.of(16, 0);
    private static final int SLOT_DURATION_MINUTES = 30;

    private final List<TimeSlot> slots;

    /**
     * Creates the standard clinic schedule and generates every slot.
     */
    public ClinicSchedule() {
        List<TimeSlot> generatedSlots = new ArrayList<>();
        LocalTime currentStartTime = OPENING_TIME;

        while (currentStartTime.isBefore(CLOSING_TIME)) {
            generatedSlots.add(new TimeSlot(currentStartTime));
            currentStartTime = currentStartTime.plusMinutes(SLOT_DURATION_MINUTES);
        }

        this.slots = Collections.unmodifiableList(generatedSlots);
    }

    /**
     * Tests whether a slot belongs to this clinic schedule.
     *
     * @param slot the slot to check
     * @return {@code true} when the slot is part of the schedule
     */
    public boolean contains(TimeSlot slot) {
        return slot != null && slots.contains(slot);
    }

    /**
     * Returns the schedule slot that starts at the supplied hour and minute.
     *
     * @param hour   the hour of the slot start
     * @param minute the minute of the slot start
     * @return the matching slot
     * @throws IllegalArgumentException if no slot starts at that time
     */
    public TimeSlot getSlot(int hour, int minute) {
        LocalTime startTime = LocalTime.of(hour, minute);

        for (TimeSlot slot : slots) {
            if (slot.getStartTime().equals(startTime)) {
                return slot;
            }
        }

        throw new IllegalArgumentException("No clinic slot starts at " + formatTime(startTime) + ".");
    }

    /**
     * Formats a time for display in a schedule description.
     *
     * @param time the time to format
     * @return the time in {@code HH:mm} form
     */
    private static String formatTime(LocalTime time) {
        return String.format("%02d:%02d", time.getHour(), time.getMinute());
    }

    /**
     * Returns a description of the clinic schedule.
     *
     * @return the schedule description
     */
    @Override
    public String toString() {
        return formatTime(OPENING_TIME) + "-" + formatTime(CLOSING_TIME)
                + ", " + SLOT_DURATION_MINUTES + "-minute slots, "
                + slots.size() + " slots";
    }
}
