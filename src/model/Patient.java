package model;

import util.Validation;

/**
 * Represents a patient who can book an appointment.
 *
 * <p>The patient's name and mobile number are required and are stored as
 * supplied. The class is intentionally small: it models the booking contact
 * details needed by an appointment.</p>
 */
public class Patient {

    private final String name;
    private final String mobileNumber;

    /**
     * Creates a patient with the supplied name and mobile number.
     *
     * @param name         the patient's name
     * @param mobileNumber the patient's mobile number
     * @throws IllegalArgumentException if the name or mobile number is blank
     */
    public Patient(String name, String mobileNumber) {
        this.name = Validation.requireNonBlank(name, "Patient name");
        this.mobileNumber = Validation.requireNonBlank(mobileNumber, "Patient mobile number");
    }

    /**
     * Returns the patient's name.
     *
     * @return the patient name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the patient's mobile number.
     *
     * @return the mobile number
     */
    public String getMobileNumber() {
        return mobileNumber;
    }

    /**
     * Returns the patient's details in a readable form.
     *
     * @return the patient description
     */
    @Override
    public String toString() {
        return name + " (mobile: " + mobileNumber + ")";
    }
}
