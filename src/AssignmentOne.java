import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import model.Appointment;
import model.ClinicSchedule;
import model.Dietitian;
import model.GeneralPractitioner;
import model.HealthProfessional;
import model.Nurse;
import model.Patient;
import model.Specialist;
import model.TimeSlot;
import service.AppointmentManager;
import util.Weekday;

/**
 * Runs a scripted demonstration of the health service appointment system.
 *
 * <p>The {@code main} method creates a mix of health professionals, prints
 * their details through the common {@link HealthProfessional} type, and then
 * exercises the appointment manager with normal bookings, filtering, sorting,
 * cancellation, and error cases.</p>
 */
public class AssignmentOne {

    /**
     * Private constructor prevents this demonstration class from being
     * instantiated.
     */
    private AssignmentOne() {
    }

    /**
     * Starts the demonstration.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("PROG2004 Assessment 1 - Health Service Appointment System");
        System.out.println("============================================================");

        // Three general practitioners and one of each other type give a
        // realistic mix without making the demonstration unnecessarily long.
        GeneralPractitioner gp1 = new GeneralPractitioner(
                "1001", "Amelia Clarke",
                workingDays(Weekday.MONDAY, Weekday.WEDNESDAY, Weekday.FRIDAY),
                "MED-1001", true);

        GeneralPractitioner gp2 = new GeneralPractitioner(
                "1002", "Noah Bennett",
                workingDays(Weekday.TUESDAY, Weekday.THURSDAY),
                "MED-1002", false);

        GeneralPractitioner gp3 = new GeneralPractitioner(
                "1003", "Priya Nair",
                workingDays(Weekday.MONDAY, Weekday.TUESDAY, Weekday.THURSDAY),
                "MED-1003", true);

        Specialist specialist1 = new Specialist(
                "2001", "Sophia Chen",
                workingDays(Weekday.WEDNESDAY, Weekday.FRIDAY),
                "Cardiology", 220.00);

        Nurse nurse1 = new Nurse(
                "3001", "Grace Miller",
                workingDays(Weekday.MONDAY, Weekday.TUESDAY, Weekday.WEDNESDAY,
                        Weekday.THURSDAY, Weekday.FRIDAY),
                "General Practice", "Morning");

        Dietitian dietitian1 = new Dietitian(
                "4001", "Olivia Grant",
                workingDays(Weekday.MONDAY, Weekday.WEDNESDAY),
                "Sports Nutrition", true);

        List<HealthProfessional> professionals = new ArrayList<>();
        professionals.add(gp1);
        professionals.add(gp2);
        professionals.add(gp3);
        professionals.add(specialist1);
        professionals.add(nurse1);
        professionals.add(dietitian1);

        Patient demoPatient = new Patient("Jordan Lee", "0412 345 678");

        printSection("PARTS 1 AND 2 - Class design and specialisation");
        System.out.println("HealthProfessional is abstract. It holds the common ID, name, and");
        System.out.println("working days, and each subclass supplies its role and behaviour.");
        System.out.println("Concrete types: GeneralPractitioner, Specialist, Nurse, Dietitian.");
        System.out.println();
        System.out.println("Type-specific behaviour:");

        // Each concrete type exposes behaviour that the others do not have.
        System.out.println("  - " + gp1.prescribe(demoPatient, "Amoxicillin", "500 mg three times daily"));
        System.out.println("  - " + specialist1.orderDiagnosticTest(demoPatient, "an electrocardiogram (ECG)"));
        System.out.println("  - " + nurse1.takeBloodSample(demoPatient, "full blood count"));
        System.out.println("  - " + dietitian1.createMealPlan(demoPatient, "lower sodium intake"));

        printSection("PART 3 - Working with health professional objects");
        System.out.println("All professionals are stored in one List<HealthProfessional>:");

        for (HealthProfessional professional : professionals) {
            System.out.println();
            System.out.println(indent(professional.toString(), "  "));
        }

        System.out.println();
        System.out.println("Polymorphic performRoleDuty() calls through the common type:");
        for (HealthProfessional professional : professionals) {
            System.out.println("  - " + professional.performRoleDuty(demoPatient));
        }

        ClinicSchedule schedule = new ClinicSchedule();
        demonstrateAppointmentSystem(professionals, schedule);

        System.out.println();
        System.out.println("End of demonstration.");
    }

    /**
     * Demonstrates appointment booking, filtering, sorting, and cancellation.
     *
     * @param professionals the health professionals created for the demo
     * @param schedule      the clinic schedule to use
     */
    private static void demonstrateAppointmentSystem(List<HealthProfessional> professionals,
            ClinicSchedule schedule) {
        AppointmentManager manager = new AppointmentManager(schedule);

        HealthProfessional gp1 = findProfessional(professionals, "1001");
        HealthProfessional gp2 = findProfessional(professionals, "1002");
        HealthProfessional gp3 = findProfessional(professionals, "1003");
        HealthProfessional specialist1 = findProfessional(professionals, "2001");
        HealthProfessional nurse1 = findProfessional(professionals, "3001");
        HealthProfessional dietitian1 = findProfessional(professionals, "4001");

        Patient jordan = new Patient("Jordan Lee", "0412 345 678");
        Patient mia = new Patient("Mia Chen", "0423 456 789");
        Patient noah = new Patient("Noah Wilson", "0434 567 890");
        Patient ava = new Patient("Ava Patel", "0455 678 901");

        printSection("PARTS 4, 5 AND 6 - Appointments");
        System.out.println("An Appointment links a Patient, a HealthProfessional, and a TimeSlot.");
        System.out.println("AppointmentManager stores them in an ArrayList and enforces booking rules.");
        System.out.println("Clinic schedule: " + schedule);
        System.out.println();

        System.out.println("Adding appointments:");
        System.out.println("  " + manager.addAppointment(jordan, gp1, schedule.getSlot(9, 0)));
        System.out.println("  " + manager.addAppointment(mia, specialist1, schedule.getSlot(9, 30)));
        System.out.println("  " + manager.addAppointment(noah, nurse1, schedule.getSlot(10, 0)));
        System.out.println("  " + manager.addAppointment(jordan, dietitian1, schedule.getSlot(11, 0)));
        System.out.println("  " + manager.addAppointment(mia, gp2, schedule.getSlot(14, 0)));
        System.out.println("  " + manager.addAppointment(noah, gp3, schedule.getSlot(9, 0)));
        System.out.println("  " + manager.addAppointment(ava, specialist1, schedule.getSlot(13, 0)));

        System.out.println();
        System.out.println("Attempting invalid bookings:");

        // The same professional is already booked at 09:00.
        System.out.println("  " + manager.addAppointment(mia, gp1, schedule.getSlot(9, 0)));

        // 17:00 is outside the clinic's 08:00-16:00 schedule.
        System.out.println("  " + manager.addAppointment(jordan, gp1,
                new TimeSlot(LocalTime.of(17, 0))));

        // This professional does not work today.
        HealthProfessional unavailableGp = new GeneralPractitioner(
                "1099", "Riley Adams", daysWithoutToday(), "MED-1099", true);
        System.out.println("  " + manager.addAppointment(jordan, unavailableGp, schedule.getSlot(12, 0)));

        printAppointments("All appointments (sorted earliest to latest):",
                manager.getAllAppointments());
        printAppointments("Filtered by health professional ID 1001:",
                manager.getAppointmentsForProfessional("1001"));
        printAppointments("Filtered by patient mobile 0423 456 789:",
                manager.getAppointmentsForPatient("0423 456 789"));

        System.out.println();
        System.out.println("Cancelling the 09:00 appointment for health professional 1001:");
        System.out.println("  " + manager.cancelAppointment("1001", schedule.getSlot(9, 0)));
        printAppointments("All appointments after the cancellation:",
                manager.getAllAppointments());

        System.out.println();
        System.out.println("Cancelling all appointments for patient 0423 456 789:");
        System.out.println(indent(manager.cancelAppointmentsForPatient("0423 456 789"), "  "));
        printAppointments("All appointments after the patient cancellation:",
                manager.getAllAppointments());

        System.out.println();
        System.out.println("Attempting invalid cancellations:");
        System.out.println("  " + manager.cancelAppointment("9999", schedule.getSlot(12, 0)));
        System.out.println("  " + manager.cancelAppointmentsForPatient("0499 999 999"));
        System.out.println("  " + manager.cancelAppointmentsForPatient(""));

        System.out.println();
        System.out.println("Appointments remaining: " + manager.getAppointmentCount());
    }

    /**
     * Returns the health professional with the supplied identifier.
     *
     * @param professionals the professionals to search
     * @param id            the required identifier
     * @return the matching professional
     * @throws IllegalArgumentException if no professional has that identifier
     */
    private static HealthProfessional findProfessional(List<HealthProfessional> professionals, String id) {
        for (HealthProfessional professional : professionals) {
            if (professional.getId().equals(id)) {
                return professional;
            }
        }

        throw new IllegalArgumentException("No health professional with ID " + id + ".");
    }

    /**
     * Builds a set of working days that includes today.
     *
     * <p>This is a demo helper: it guarantees that each professional can be
     * booked on the day the demonstration runs, regardless of the actual
     * weekday.</p>
     *
     * @param preferredDays the days to include in addition to today
     * @return the working-day set
     */
    private static Set<Weekday> workingDays(Weekday... preferredDays) {
        Set<Weekday> days = new LinkedHashSet<>();
        days.add(Weekday.today());

        for (Weekday day : preferredDays) {
            if (day != null) {
                days.add(day);
            }
        }

        return days;
    }

    /**
     * Builds a set containing every weekday except today.
     *
     * <p>This is used to create a professional who cannot be booked on the
     * current day for the error demonstration.</p>
     *
     * @return the working-day set without today
     */
    private static Set<Weekday> daysWithoutToday() {
        Set<Weekday> days = new LinkedHashSet<>();

        for (Weekday day : Weekday.values()) {
            if (day != Weekday.today()) {
                days.add(day);
            }
        }

        return days;
    }

    /**
     * Prints a title followed by a list of appointments.
     *
     * @param title        the section title
     * @param appointments the appointments to print
     */
    private static void printAppointments(String title, List<Appointment> appointments) {
        System.out.println();
        System.out.println(title);

        if (appointments.isEmpty()) {
            System.out.println("  (no appointments)");
            return;
        }

        for (int index = 0; index < appointments.size(); index++) {
            System.out.println("  " + (index + 1) + ".");
            System.out.println(indent(appointments.get(index).toString(), "     "));
        }
    }

    /**
     * Indents every line of a block of text.
     *
     * @param text   the text to indent
     * @param prefix the prefix placed before each line
     * @return the indented text
     */
    private static String indent(String text, String prefix) {
        String[] lines = text.split("\n");
        StringBuilder result = new StringBuilder();

        for (int index = 0; index < lines.length; index++) {
            if (index > 0) {
                result.append("\n");
            }
            result.append(prefix).append(lines[index]);
        }

        return result.toString();
    }

    /**
     * Prints a section heading.
     *
     * @param title the section title
     */
    private static void printSection(String title) {
        System.out.println();
        System.out.println("============================================================");
        System.out.println(title);
        System.out.println("============================================================");
    }
}
