package model;

import java.util.Set;

import util.Validation;
import util.Weekday;

/**
 * Represents a nurse who works in a clinic ward or outpatient area.
 *
 * <p>A nurse adds a ward and shift to the common health professional data, and
 * can take blood samples from patients.</p>
 */
public class Nurse extends HealthProfessional {

    private final String ward;
    private final String shift;

    /**
     * Creates a nurse with all details supplied.
     *
     * @param id            the numeric identifier
     * @param name          the nurse's name
     * @param availableDays the days the nurse works
     * @param ward          the ward in which the nurse works
     * @param shift         the nurse's shift
     * @throws IllegalArgumentException if the ward or shift is blank
     */
    public Nurse(String id, String name, Set<Weekday> availableDays,
            String ward, String shift) {
        super(id, name, availableDays);
        this.ward = Validation.requireNonBlank(ward, "Ward");
        this.shift = Validation.requireNonBlank(shift, "Shift");
    }

    /**
     * Returns the ward in which the nurse works.
     *
     * @return the ward
     */
    public String getWard() {
        return ward;
    }

    /**
     * Returns the nurse's shift.
     *
     * @return the shift
     */
    public String getShift() {
        return shift;
    }

    /**
     * Returns the role name used in output.
     *
     * @return {@code "Nurse"}
     */
    @Override
    public String getRole() {
        return "Nurse";
    }

    /**
     * Returns the ward and shift details specific to a nurse.
     *
     * @return the role-specific details
     */
    @Override
    public String getRoleDetails() {
        return "Ward: " + ward + " | Shift: " + shift;
    }

    /**
     * Performs a nursing duty for a patient.
     *
     * @param patient the patient being cared for
     * @return a description of the duty
     */
    @Override
    public String performRoleDuty(Patient patient) {
        requirePatient(patient);
        return getRole() + " " + getName() + " takes a blood sample from "
                + patient.getName() + ".";
    }

    /**
     * Takes a blood sample from a patient.
     *
     * @param patient    the patient providing the sample
     * @param sampleType the type of sample being collected
     * @return a description of the sample collection
     * @throws IllegalArgumentException if the patient or sample type is missing
     */
    public String takeBloodSample(Patient patient, String sampleType) {
        requirePatient(patient);
        String safeSampleType = Validation.requireNonBlank(sampleType, "Sample type");

        return getRole() + " " + getName() + " collects a " + safeSampleType
                + " sample from " + patient.getName() + ".";
    }
}
