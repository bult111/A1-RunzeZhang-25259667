package model;

/**
 * Represents a single appointment that links a patient to a health
 * professional at a specific time.
 *
 * <p>The appointment stores a {@link Patient}, a
 * {@link HealthProfessional} reference, and a {@link TimeSlot}. Because the
 * professional is referenced through the abstract base type, one appointment
 * class can work with every kind of health professional in the system.</p>
 *
 * <p>Bookings are only made for the current day, so the date is not stored.
 * {@code Appointment} implements {@link Comparable} so a collection of
 * appointments can be sorted from earliest to latest.</p>
 */
public class Appointment implements Comparable<Appointment> {

    private final Patient patient;
    private final HealthProfessional professional;
    private final TimeSlot timeSlot;

    /**
     * Creates an appointment for the current day.
     *
     * @param patient      the patient booking the appointment
     * @param professional the health professional being seen
     * @param timeSlot     the appointment time slot
     * @throws IllegalArgumentException if any required value is missing
     */
    public Appointment(Patient patient, HealthProfessional professional, TimeSlot timeSlot) {
        if (patient == null) {
            throw new IllegalArgumentException("An appointment requires a patient.");
        }
        if (professional == null) {
            throw new IllegalArgumentException("An appointment requires a health professional.");
        }
        if (timeSlot == null) {
            throw new IllegalArgumentException("An appointment requires a time slot.");
        }

        this.patient = patient;
        this.professional = professional;
        this.timeSlot = timeSlot;
    }

    /**
     * Returns the patient for this appointment.
     *
     * @return the patient
     */
    public Patient getPatient() {
        return patient;
    }

    /**
     * Returns the health professional for this appointment.
     *
     * @return the health professional
     */
    public HealthProfessional getProfessional() {
        return professional;
    }

    /**
     * Returns the appointment time slot.
     *
     * @return the time slot
     */
    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    /**
     * Compares appointments by their time slots.
     *
     * @param other the appointment to compare with
     * @return a negative number, zero, or a positive number if this appointment
     *         is earlier than, equal to, or later than the other appointment
     * @throws NullPointerException if {@code other} is {@code null}
     */
    @Override
    public int compareTo(Appointment other) {
        if (other == null) {
            throw new NullPointerException("Cannot compare an appointment with null.");
        }
        return timeSlot.compareTo(other.timeSlot);
    }

    /**
     * Returns the appointment details, including the chosen professional's
     * shared and role-specific information.
     *
     * @return the appointment description
     */
    @Override
    public String toString() {
        return "Appointment at " + timeSlot.getLabel() + "\n"
                + "  Patient: " + patient + "\n"
                + "  Health professional: " + professional.getSummary() + "\n"
                + "    " + professional.getRoleDetails();
    }
}
