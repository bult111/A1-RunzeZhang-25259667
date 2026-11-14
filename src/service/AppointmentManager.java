package service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.Appointment;
import model.ClinicSchedule;
import model.HealthProfessional;
import model.Patient;
import model.TimeSlot;
import util.Validation;
import util.Weekday;

/**
 * Manages the clinic's collection of appointments.
 *
 * <p>An {@link ArrayList} is used because appointments are kept in a
 * collection that is displayed and sorted by time, and because the list can
 * grow as bookings are added. Every operation that can fail returns a
 * {@code WARNING} message instead of silently doing nothing, while successful
 * operations return a {@code SUCCESS} message.</p>
 *
 * <p>The manager owns the booking rules: an appointment must use a slot from
 * the clinic schedule, the professional must work on the current day, and the
 * same professional cannot be booked into the same slot twice.</p>
 */
public class AppointmentManager {

    private final List<Appointment> appointments;
    private final ClinicSchedule schedule;

    /**
     * Creates a manager using the supplied clinic schedule.
     *
     * @param schedule the schedule that defines valid appointment slots
     * @throws IllegalArgumentException if the schedule is {@code null}
     */
    public AppointmentManager(ClinicSchedule schedule) {
        if (schedule == null) {
            throw new IllegalArgumentException("A clinic schedule is required.");
        }

        this.schedule = schedule;
        this.appointments = new ArrayList<>();
    }

    /**
     * Adds an appointment after checking the required booking rules.
     *
     * @param patient      the patient booking the appointment
     * @param professional the health professional being seen
     * @param timeSlot     the requested time slot
     * @return a success message, or a warning if the appointment cannot be added
     */
    public String addAppointment(Patient patient, HealthProfessional professional, TimeSlot timeSlot) {
        if (patient == null) {
            return "WARNING: A patient must be supplied.";
        }
        if (professional == null) {
            return "WARNING: A health professional must be supplied.";
        }
        if (timeSlot == null) {
            return "WARNING: A time slot must be supplied.";
        }
        if (!professional.isAvailableOn(Weekday.today())) {
            return "WARNING: " + professional.getSummary() + " does not work on "
                    + Weekday.today() + ".";
        }
        if (!schedule.contains(timeSlot)) {
            return "WARNING: " + timeSlot.getLabel()
                    + " is outside the clinic schedule (" + schedule + ").";
        }
        if (findConflictingAppointment(professional, timeSlot) != null) {
            return "WARNING: " + professional.getName() + " is already booked at "
                    + timeSlot.getLabel() + ".";
        }

        Appointment appointment = new Appointment(patient, professional, timeSlot);
        appointments.add(appointment);

        return "SUCCESS: " + patient.getName() + " booked with "
                + professional.getSummary() + " at " + timeSlot.getLabel() + ".";
    }

    /**
     * Returns every appointment sorted from earliest to latest.
     *
     * @return an unmodifiable, time-ordered list of appointments
     */
    public List<Appointment> getAllAppointments() {
        return sortedCopy(appointments);
    }

    /**
     * Returns the appointments for one health professional, sorted by time.
     *
     * @param professionalId the health professional's numeric identifier
     * @return an unmodifiable, time-ordered list of matching appointments
     */
    public List<Appointment> getAppointmentsForProfessional(String professionalId) {
        List<Appointment> matches = new ArrayList<>();

        if (!Validation.isBlank(professionalId)) {
            String requestedId = professionalId.trim();

            for (Appointment appointment : appointments) {
                if (appointment.getProfessional().getId().equals(requestedId)) {
                    matches.add(appointment);
                }
            }
        }

        return sortedCopy(matches);
    }

    /**
     * Returns the appointments for one patient, sorted by time.
     *
     * <p>The mobile number is compared with the value stored on the patient.
     * If no appointment matches, an empty list is returned.</p>
     *
     * @param mobileNumber the patient's mobile number
     * @return an unmodifiable, time-ordered list of matching appointments
     */
    public List<Appointment> getAppointmentsForPatient(String mobileNumber) {
        List<Appointment> matches = new ArrayList<>();

        if (!Validation.isBlank(mobileNumber)) {
            String requestedNumber = mobileNumber.trim();

            for (Appointment appointment : appointments) {
                if (appointment.getPatient().getMobileNumber().equals(requestedNumber)) {
                    matches.add(appointment);
                }
            }
        }

        return sortedCopy(matches);
    }

    /**
     * Cancels the appointment for a health professional at a particular time.
     *
     * @param professionalId the health professional's numeric identifier
     * @param timeSlot       the appointment time slot
     * @return a success message, or a warning if no matching appointment exists
     */
    public String cancelAppointment(String professionalId, TimeSlot timeSlot) {
        if (Validation.isBlank(professionalId)) {
            return "WARNING: A health professional ID must be supplied.";
        }
        if (timeSlot == null) {
            return "WARNING: A time slot must be supplied.";
        }

        String requestedId = professionalId.trim();

        for (int index = 0; index < appointments.size(); index++) {
            Appointment appointment = appointments.get(index);

            if (appointment.getProfessional().getId().equals(requestedId)
                    && appointment.getTimeSlot().equals(timeSlot)) {
                appointments.remove(index);
                return "SUCCESS: Cancelled " + appointment.getPatient().getName()
                        + "'s appointment with " + appointment.getProfessional().getSummary()
                        + " at " + appointment.getTimeSlot().getLabel() + ".";
            }
        }

        return "WARNING: No appointment found for health professional ID " + requestedId
                + " at " + timeSlot.getLabel() + ".";
    }

    /**
     * Cancels every appointment belonging to a patient, identified by mobile
     * number.
     *
     * <p>This method deliberately cancels all matching appointments and reports
     * how many were removed, so the caller is not left unsure about the
     * result.</p>
     *
     * @param mobileNumber the patient's mobile number
     * @return a success message, or a warning if no matching appointment exists
     */
    public String cancelAppointmentsForPatient(String mobileNumber) {
        if (Validation.isBlank(mobileNumber)) {
            return "WARNING: A patient mobile number must be supplied.";
        }

        String requestedNumber = mobileNumber.trim();
        List<Appointment> cancelled = new ArrayList<>();

        for (int index = 0; index < appointments.size();) {
            Appointment appointment = appointments.get(index);

            if (appointment.getPatient().getMobileNumber().equals(requestedNumber)) {
                appointments.remove(index);
                cancelled.add(appointment);
            } else {
                index++;
            }
        }

        if (cancelled.isEmpty()) {
            return "WARNING: No appointments found for patient " + requestedNumber + ".";
        }

        StringBuilder message = new StringBuilder("SUCCESS: Cancelled ")
                .append(cancelled.size())
                .append(" appointment(s) for ")
                .append(requestedNumber)
                .append(".");

        for (Appointment appointment : cancelled) {
            message.append("\n  - ")
                    .append(appointment.getTimeSlot().getLabel())
                    .append(" with ")
                    .append(appointment.getProfessional().getSummary());
        }

        return message.toString();
    }

    /**
     * Returns the number of appointments currently managed.
     *
     * @return the appointment count
     */
    public int getAppointmentCount() {
        return appointments.size();
    }

    /**
     * Finds an existing appointment that would clash with a proposed booking.
     *
     * @param professional the proposed health professional
     * @param timeSlot     the proposed time slot
     * @return the conflicting appointment, or {@code null} when there is no clash
     */
    private Appointment findConflictingAppointment(HealthProfessional professional, TimeSlot timeSlot) {
        for (Appointment appointment : appointments) {
            if (appointment.getProfessional().equals(professional)
                    && appointment.getTimeSlot().equals(timeSlot)) {
                return appointment;
            }
        }

        return null;
    }

    /**
     * Creates an unmodifiable, time-ordered copy of a list of appointments.
     *
     * @param source the appointments to copy and sort
     * @return the sorted, unmodifiable copy
     */
    private static List<Appointment> sortedCopy(List<Appointment> source) {
        List<Appointment> copy = new ArrayList<>(source);
        Collections.sort(copy);
        return Collections.unmodifiableList(copy);
    }
}
