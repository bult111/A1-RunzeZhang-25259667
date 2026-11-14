package model;

import java.util.Set;

import util.Validation;
import util.Weekday;

/**
 * Represents a general practitioner, a doctor who provides general medical
 * care and can prescribe medication.
 *
 * <p>The class adds the doctor's medical licence number and whether they bulk
 * bill, while inheriting the common identity, name, and availability data from
 * {@link HealthProfessional}.</p>
 */
public class GeneralPractitioner extends HealthProfessional {

    private final String medicalLicenceNumber;
    private final boolean bulkBilling;

    /**
     * Creates a general practitioner with all details supplied.
     *
     * @param id                   the numeric identifier
     * @param name                 the doctor's name
     * @param availableDays        the days the doctor works
     * @param medicalLicenceNumber the doctor's medical licence number
     * @param bulkBilling          whether the doctor bulk bills
     * @throws IllegalArgumentException if the licence number is blank
     */
    public GeneralPractitioner(String id, String name, Set<Weekday> availableDays,
            String medicalLicenceNumber, boolean bulkBilling) {
        super(id, name, availableDays);
        this.medicalLicenceNumber = Validation.requireNonBlank(
                medicalLicenceNumber, "Medical licence number");
        this.bulkBilling = bulkBilling;
    }

    /**
     * Returns the doctor's medical licence number.
     *
     * @return the licence number
     */
    public String getMedicalLicenceNumber() {
        return medicalLicenceNumber;
    }

    /**
     * Tests whether the doctor bulk bills.
     *
     * @return {@code true} when the doctor bulk bills
     */
    public boolean isBulkBilling() {
        return bulkBilling;
    }

    /**
     * Returns the role name used in output.
     *
     * @return {@code "General Practitioner"}
     */
    @Override
    public String getRole() {
        return "General Practitioner";
    }

    /**
     * Returns the licence and billing details specific to a general
     * practitioner.
     *
     * @return the role-specific details
     */
    @Override
    public String getRoleDetails() {
        return "Medical licence: " + medicalLicenceNumber
                + " | Bulk billing: " + (bulkBilling ? "Yes" : "No");
    }

    /**
     * Performs a general consultation duty for a patient.
     *
     * @param patient the patient being seen
     * @return a description of the duty
     */
    @Override
    public String performRoleDuty(Patient patient) {
        requirePatient(patient);
        return getRole() + " " + getName() + " reviews " + patient.getName()
                + "'s symptoms and prescribes medication.";
    }

    /**
     * Prescribes a medication for a patient.
     *
     * @param patient    the patient receiving the prescription
     * @param medication the medication name
     * @param dosage     the dosage instructions
     * @return a description of the prescription
     * @throws IllegalArgumentException if the patient, medication, or dosage is
     *                                  missing
     */
    public String prescribe(Patient patient, String medication, String dosage) {
        requirePatient(patient);
        String safeMedication = Validation.requireNonBlank(medication, "Medication");
        String safeDosage = Validation.requireNonBlank(dosage, "Dosage");

        return getRole() + " " + getName() + " prescribes " + safeMedication
                + " (" + safeDosage + ") to " + patient.getName() + ".";
    }
}
