package model;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import util.Validation;
import util.Weekday;

/**
 * Represents the information and behaviour shared by every health
 * professional.
 *
 * <p>This class is abstract because a health professional must have a concrete
 * role. Subclasses supply their role name, extra details, and role-specific
 * behaviour while inheriting the common identity, name, and availability
 * data.</p>
 *
 * <p>Fields are private and controlled through validated accessors, so an
 * object cannot be put into an invalid state from outside the class.</p>
 */
public abstract class HealthProfessional {

    private final String id;
    private final String name;
    private final Set<Weekday> availableDays;

    /**
     * Creates a health professional with an explicit set of working days.
     *
     * @param id            the professional's numeric identifier
     * @param name          the professional's name
     * @param availableDays the days on which the professional works
     * @throws IllegalArgumentException if the ID is not numeric, the name is
     *                                  blank, or no working days are supplied
     */
    protected HealthProfessional(String id, String name, Set<Weekday> availableDays) {
        this.id = Validation.requireNumeric(id, "Health professional ID");
        this.name = Validation.requireNonBlank(name, "Health professional name");
        this.availableDays = copyAvailableDays(availableDays);
    }

    /**
     * Returns the professional's numeric identifier.
     *
     * @return the identifier
     */
    public final String getId() {
        return id;
    }

    /**
     * Returns the professional's name.
     *
     * @return the name
     */
    public final String getName() {
        return name;
    }

    /**
     * Returns the days on which the professional works.
     *
     * <p>The returned set is unmodifiable so availability cannot be changed
     * from outside the class.</p>
     *
     * @return the working days in Monday-to-Sunday order
     */
    public final Set<Weekday> getAvailableDays() {
        return availableDays;
    }

    /**
     * Tests whether the professional works on a given day.
     *
     * @param day the day to check
     * @return {@code true} when the professional works that day
     */
    public final boolean isAvailableOn(Weekday day) {
        return day != null && availableDays.contains(day);
    }

    /**
     * Returns the name of this professional's role.
     *
     * @return the role name, such as {@code "General Practitioner"}
     */
    public abstract String getRole();

    /**
     * Returns the details that are specific to this type of professional.
     *
     * <p>This method is called by {@link #toString()} so each subclass can add
     * its own information without duplicating the common details.</p>
     *
     * @return the role-specific details
     */
    public abstract String getRoleDetails();

    /**
     * Performs this professional's characteristic duty for a patient.
     *
     * <p>Calling this method through a {@code HealthProfessional} reference
     * invokes the version supplied by the object's actual subclass, which is
     * polymorphism in action.</p>
     *
     * @param patient the patient receiving the service
     * @return a description of the duty performed
     */
    public abstract String performRoleDuty(Patient patient);

    /**
     * Returns a short one-line description of the professional.
     *
     * @return the role, name, and identifier
     */
    public String getSummary() {
        return getRole() + " " + name + " (ID " + id + ")";
    }

    /**
     * Validates and defensively copies a set of working days.
     *
     * @param days the supplied days
     * @return an unmodifiable set in Monday-to-Sunday order
     * @throws IllegalArgumentException if the set is {@code null} or empty
     */
    private static Set<Weekday> copyAvailableDays(Set<Weekday> days) {
        if (days == null || days.isEmpty()) {
            throw new IllegalArgumentException("At least one available day must be supplied.");
        }

        Set<Weekday> orderedDays = new LinkedHashSet<>();
        for (Weekday day : Weekday.values()) {
            if (days.contains(day)) {
                orderedDays.add(day);
            }
        }

        if (orderedDays.isEmpty()) {
            throw new IllegalArgumentException("At least one valid available day must be supplied.");
        }

        return Collections.unmodifiableSet(orderedDays);
    }

    /**
     * Ensures that a patient was supplied to a role-specific operation.
     *
     * @param patient the patient to check
     * @return the validated patient
     * @throws IllegalArgumentException if {@code patient} is {@code null}
     */
    protected final Patient requirePatient(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("A patient is required to perform this duty.");
        }
        return patient;
    }

    /**
     * Formats the working days for display.
     *
     * @return the comma-separated working days
     */
    private String formatAvailableDays() {
        StringBuilder text = new StringBuilder();

        for (Weekday day : availableDays) {
            if (text.length() > 0) {
                text.append(", ");
            }
            text.append(day.getDisplayName());
        }

        return text.toString();
    }

    /**
     * Returns all common and role-specific details in a readable form.
     *
     * @return the professional's details
     */
    @Override
    public String toString() {
        return getRole() + " (ID: " + id + ")\n"
                + "  Name: " + name + "\n"
                + "  Available days: " + formatAvailableDays() + "\n"
                + "  " + getRoleDetails();
    }

    /**
     * Tests whether this professional is the same as another object.
     *
     * <p>Two health professionals are considered equal when their identifiers
     * match. This is used by the appointment manager to detect double
     * bookings for the same professional.</p>
     *
     * @param other the object to compare with
     * @return {@code true} when both objects have the same identifier
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof HealthProfessional)) {
            return false;
        }

        HealthProfessional professional = (HealthProfessional) other;
        return id.equals(professional.id);
    }

    /**
     * Returns a hash code consistent with {@link #equals(Object)}.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
