package model;

import java.util.Set;

import util.Validation;
import util.Weekday;

/**
 * Represents a specialist doctor, such as a cardiologist or dermatologist.
 *
 * <p>A specialist adds a medical specialty and a consultation fee to the
 * common health professional data, and can order diagnostic tests.</p>
 */
public class Specialist extends HealthProfessional {

    private final String specialty;
    private final double consultationFee;

    /**
     * Creates a specialist with all details supplied.
     *
     * @param id              the numeric identifier
     * @param name            the specialist's name
     * @param availableDays   the days the specialist works
     * @param specialty       the specialist's medical specialty
     * @param consultationFee the fee charged for a consultation
     * @throws IllegalArgumentException if the specialty is blank or the fee is
     *                                  negative or not a finite number
     */
    public Specialist(String id, String name, Set<Weekday> availableDays,
            String specialty, double consultationFee) {
        super(id, name, availableDays);

        if (Double.isNaN(consultationFee) || Double.isInfinite(consultationFee) || consultationFee < 0) {
            throw new IllegalArgumentException("Consultation fee must be zero or greater.");
        }

        this.specialty = Validation.requireNonBlank(specialty, "Specialty");
        this.consultationFee = consultationFee;
    }

    /**
     * Returns the specialist's medical specialty.
     *
     * @return the specialty
     */
    public String getSpecialty() {
        return specialty;
    }

    /**
     * Returns the specialist's consultation fee.
     *
     * @return the consultation fee
     */
    public double getConsultationFee() {
        return consultationFee;
    }

    /**
     * Returns the role name used in output.
     *
     * @return {@code "Specialist"}
     */
    @Override
    public String getRole() {
        return "Specialist";
    }

    /**
     * Returns the specialty and fee details specific to a specialist.
     *
     * @return the role-specific details
     */
    @Override
    public String getRoleDetails() {
        return "Specialty: " + specialty
                + " | Consultation fee: " + String.format("$%.2f", consultationFee);
    }

    /**
     * Performs a specialist consultation duty for a patient.
     *
     * @param patient the patient being seen
     * @return a description of the duty
     */
    @Override
    public String performRoleDuty(Patient patient) {
        requirePatient(patient);
        return getRole() + " " + getName() + " provides a " + specialty
                + " consultation for " + patient.getName() + ".";
    }

    /**
     * Orders a diagnostic test for a patient.
     *
     * @param patient  the patient receiving the test
     * @param testName the name of the test
     * @return a description of the test order
     * @throws IllegalArgumentException if the patient or test name is missing
     */
    public String orderDiagnosticTest(Patient patient, String testName) {
        requirePatient(patient);
        String safeTestName = Validation.requireNonBlank(testName, "Test name");

        return getRole() + " " + getName() + " orders " + safeTestName
                + " for " + patient.getName() + ".";
    }
}
