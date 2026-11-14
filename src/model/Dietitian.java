package model;

import java.util.Set;

import util.Validation;
import util.Weekday;

/**
 * Represents a dietitian who provides nutrition advice and meal plans.
 *
 * <p>A dietitian adds a specialisation and a referral setting to the common
 * health professional data, and can create meal plans for patients.</p>
 */
public class Dietitian extends HealthProfessional {

    private final String specialisation;
    private final boolean acceptsReferrals;

    /**
     * Creates a dietitian with all details supplied.
     *
     * @param id               the numeric identifier
     * @param name             the dietitian's name
     * @param availableDays    the days the dietitian works
     * @param specialisation   the dietitian's area of specialisation
     * @param acceptsReferrals whether the dietitian accepts referrals
     * @throws IllegalArgumentException if the specialisation is blank
     */
    public Dietitian(String id, String name, Set<Weekday> availableDays,
            String specialisation, boolean acceptsReferrals) {
        super(id, name, availableDays);
        this.specialisation = Validation.requireNonBlank(specialisation, "Specialisation");
        this.acceptsReferrals = acceptsReferrals;
    }

    /**
     * Returns the dietitian's area of specialisation.
     *
     * @return the specialisation
     */
    public String getSpecialisation() {
        return specialisation;
    }

    /**
     * Tests whether the dietitian accepts referrals.
     *
     * @return {@code true} when referrals are accepted
     */
    public boolean isAcceptingReferrals() {
        return acceptsReferrals;
    }

    /**
     * Returns the role name used in output.
     *
     * @return {@code "Dietitian"}
     */
    @Override
    public String getRole() {
        return "Dietitian";
    }

    /**
     * Returns the specialisation and referral details specific to a dietitian.
     *
     * @return the role-specific details
     */
    @Override
    public String getRoleDetails() {
        return "Specialisation: " + specialisation
                + " | Accepts referrals: " + (acceptsReferrals ? "Yes" : "No");
    }

    /**
     * Performs a dietitian duty for a patient.
     *
     * @param patient the patient being advised
     * @return a description of the duty
     */
    @Override
    public String performRoleDuty(Patient patient) {
        requirePatient(patient);
        return getRole() + " " + getName() + " prepares a nutrition plan for "
                + patient.getName() + ".";
    }

    /**
     * Creates a meal plan for a patient.
     *
     * @param patient the patient receiving the plan
     * @param goal    the nutrition goal for the plan
     * @return a description of the meal plan
     * @throws IllegalArgumentException if the patient or goal is missing
     */
    public String createMealPlan(Patient patient, String goal) {
        requirePatient(patient);
        String safeGoal = Validation.requireNonBlank(goal, "Meal plan goal");

        return getRole() + " " + getName() + " creates a meal plan for "
                + patient.getName() + " focused on " + safeGoal + ".";
    }
}
