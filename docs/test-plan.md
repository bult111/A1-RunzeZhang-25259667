# PROG2004 Assessment 1 — Test Plan

## 1. Scope

This test plan covers the appointment system implemented in `src`:

- health professional creation and validation;
- the four professional subclasses;
- patient and time-slot value objects;
- the clinic schedule;
- the appointment model;
- appointment adding, filtering, sorting, and cancellation;
- the end-to-end console demonstration.

The project has no GUI, login, database, or file storage, so those areas are out
of scope.

## 2. Test environment

| Item | Value |
|---|---|
| Language | Java |
| JDK used for verification | Java 21 (Temurin 21.0.13) |
| Operating system | macOS |
| Build command | `javac -Xlint:all -d out ...` |
| Run command | `java -cp out AssignmentOne` |
| Test style | Constructor/value checks plus end-to-end console scenarios |

## 3. Test approach

Testing was performed at three levels:

1. **Value and constructor checks** — invalid IDs, blank names, null values,
   empty working-day sets, invalid time slots, and null appointment parts.
2. **Manager behaviour checks** — successful booking, duplicate booking,
   outside-schedule booking, unavailable professional, filtering, sorting,
   cancellation, and “not found” cases.
3. **End-to-end console run** — `AssignmentOne.main()` is executed and the
   output is checked against the expected sections and final count.

Business failures are expected to return `WARNING` strings rather than throw
exceptions. Programmer errors such as an invalid ID or a null required
constructor argument throw `IllegalArgumentException`.

## 4. Test data

### Health professionals used in the demo

| ID | Type | Name | Notes |
|---|---|---|---|
| 1001 | GeneralPractitioner | Amelia Clarke | licence MED-1001, bulk billing |
| 1002 | GeneralPractitioner | Noah Bennett | licence MED-1002, private billing |
| 1003 | GeneralPractitioner | Priya Nair | licence MED-1003, bulk billing |
| 2001 | Specialist | Sophia Chen | Cardiology, $220.00 |
| 3001 | Nurse | Grace Miller | General Practice, Morning |
| 4001 | Dietitian | Olivia Grant | Sports Nutrition, accepts referrals |
| 1099 | GeneralPractitioner | Riley Adams | used only for the unavailable-professional error case |

The demo helper adds the current weekday to each professional's working-day set,
so all demo professionals can be booked on the day the program runs. Riley Adams
uses the opposite helper and deliberately does **not** work today.

### Patients used in the demo

| Name | Mobile |
|---|---|
| Jordan Lee | 0412 345 678 |
| Mia Chen | 0423 456 789 |
| Noah Wilson | 0434 567 890 |
| Ava Patel | 0455 678 901 |

### Time slots used in the demo

`08:00`, `08:30`, `09:00`, `09:30`, `10:00`, `10:30`, `11:00`, `13:00`,
`14:00`, `15:30`, and the invalid `17:00`.

## 5. Test cases

| ID | Part | Scenario | Input / action | Expected result | Status |
|---|---|---|---|---|---|
| HP-01 | 1 | ID must be numeric | Create a GP with ID `12A` | `IllegalArgumentException` containing “numbers only” | Pass |
| HP-02 | 1 | Name must not be blank | Create a GP with name `"  "` | `IllegalArgumentException` | Pass |
| HP-03 | 1 | Working days must not be empty | Create a GP with an empty set | `IllegalArgumentException` | Pass |
| HP-04 | 1 | Working days cannot be changed externally | `gp.getAvailableDays().add(SUNDAY)` | `UnsupportedOperationException` | Pass |
| HP-05 | 1 | Equality is based on ID | Two GP objects with ID `1001` | `equals()` true and same `hashCode()` | Pass |
| HP-06 | 1 | Shared details are readable | `gp.toString()` | Contains role, ID, name, available days, licence, billing | Pass |
| GP-01 | 2 | GP-specific behaviour | `prescribe(patient, "Amoxicillin", "500 mg...")` | Message contains GP name, medication, patient | Pass |
| SP-01 | 2 | Specialist-specific behaviour | `orderDiagnosticTest(patient, "ECG")` | Message contains specialist name, test, patient | Pass |
| NU-01 | 2 | Nurse-specific behaviour | `takeBloodSample(patient, "full blood count")` | Message contains nurse name, sample type, patient | Pass |
| DI-01 | 2 | Dietitian-specific behaviour | `createMealPlan(patient, "lower sodium")` | Message contains dietitian name, goal, patient | Pass |
| SP-02 | 2 | Invalid consultation fee | Create a specialist with fee `-1` | `IllegalArgumentException` | Pass |
| PT-01 | 4 | Valid patient | Name `Alice`, mobile `0412 345 678` | Both values stored; `toString()` contains them | Pass |
| PT-02 | 4 | Blank patient data | Blank name or blank mobile | `IllegalArgumentException` | Pass |
| TS-01 | 4 | Fixed 30-minute slot | `new TimeSlot(09:00)` | End time 09:30; label `09:00 - 09:30` | Pass |
| TS-02 | 4 | Slot equality and ordering | Two 09:00 slots; one 09:30 slot | Equal slots compare equal; 09:00 is before 09:30 | Pass |
| TS-03 | 4 | Invalid slot values | `null`, seconds in start time, `23:50` | `IllegalArgumentException` | Pass |
| CS-01 | 4/5 | Schedule generation | Loop 08:00 to 15:30 through `getSlot()` | All 16 slots are returned; `toString()` says “16 slots” | Pass |
| CS-02 | 4/5 | Schedule membership | `contains(09:00)` and `getSlot(16:00)` | Valid slot true; 16:00 throws | Pass |
| AP-01 | 4 | Required appointment parts | Construct with null patient, professional, or slot | `IllegalArgumentException` | Pass |
| AP-02 | 4 | Appointment ordering | 09:00 appointment and 11:00 appointment | Earlier appointment compares before later one | Pass |
| AM-01 | 5 | Add a valid appointment | Jordan + GP1 + 09:00 | `SUCCESS`, list size increases | Pass |
| AM-02 | 5 | Reject double booking | Same professional and slot again | `WARNING` containing “already booked” | Pass |
| AM-03 | 5 | Reject outside-schedule slot | GP1 + 17:00 | `WARNING` containing “outside the clinic schedule” | Pass |
| AM-04 | 5 | Reject professional who does not work today | Riley Adams + 12:00 | `WARNING` containing “does not work on” | Pass |
| AM-05 | 5 | Filter by professional ID | `getAppointmentsForProfessional("1001")` | Only GP1 appointments, sorted by time | Pass |
| AM-06 | 5 | Filter by patient mobile | `getAppointmentsForPatient("0423 456 789")` | Only Mia's appointments, sorted by time | Pass |
| AM-07 | 5 | Cancel by ID + time | `cancelAppointment("1001", 09:00)` | Matching appointment removed; `SUCCESS` | Pass |
| AM-08 | 5 | Cancel by patient mobile | `cancelAppointmentsForPatient("0423 456 789")` | All Mia's appointments removed; count reported | Pass |
| AM-09 | 5 | Cancel a missing appointment | `cancelAppointment("9999", 12:00)` | `WARNING`; list unchanged | Pass |
| AM-10 | 5 | Missing mobile on cancellation | `cancelAppointmentsForPatient("")` | `WARNING`; list unchanged | Pass |
| SYS-01 | 3/6 | Polymorphic display | Run `AssignmentOne.main()` | Professional details and `performRoleDuty()` output for all six professionals | Pass |
| SYS-02 | 6 | End-to-end count | Run full demo | 7 successful bookings; 3 booking warnings; 2 cancellation operations; 3 cancellation warnings; 4 remaining | Pass |

## 6. Execution results

The final run was produced with:

```bash
javac -Xlint:all -d out src/AssignmentOne.java src/model/*.java src/service/*.java src/util/*.java
java -cp out AssignmentOne
```

Result summary:

| Item | Expected | Actual |
|---|---|---|
| Successful bookings | 7 | 7 |
| Rejected bookings | 3 | 3 |
| Cancellation operations | 2 | 2 |
| Appointments removed by cancellation | 3 | 3 |
| Rejected cancellations | 3 | 3 |
| Remaining appointments | 4 | 4 |
| Compiler warnings | 0 | 0 |
| Runtime exceptions in the normal demo | 0 | 0 |

The console output is checked by running the program. Because the demo
uses `Weekday.today()`, the printed available days and the weekday named in the
unavailable-professional warning change with the run date. The booking counts do
not change.

### Actual output excerpts

The following lines are copied from the final run. Weekday names are omitted
here because they depend on the run date.

```text
SUCCESS: Jordan Lee booked with General Practitioner Amelia Clarke (ID 1001) at 09:00 - 09:30.
SUCCESS: Mia Chen booked with Specialist Sophia Chen (ID 2001) at 09:30 - 10:00.
WARNING: Amelia Clarke is already booked at 09:00 - 09:30.
WARNING: 17:00 - 17:30 is outside the clinic schedule (08:00-16:00, 30-minute slots, 16 slots).
Filtered by health professional ID 1001:
SUCCESS: Cancelled 2 appointment(s) for 0423 456 789.
WARNING: No appointment found for health professional ID 9999 at 12:00 - 12:30.
WARNING: A patient mobile number must be supplied.
Appointments remaining: 4
End of demonstration.
```

## 7. Traceability to the assignment parts

| Assignment part | Covered by |
|---|---|
| Part 1 — health professional model | HP-01 to HP-06 |
| Part 2 — specialisation | GP-01, SP-01, SP-02, NU-01, DI-01 |
| Part 3 — working with objects | SYS-01 |
| Part 4 — appointment model | PT-01, PT-02, TS-01 to TS-03, CS-01, CS-02, AP-01, AP-02 |
| Part 5 — appointment manager | AM-01 to AM-10 |
| Part 6 — end-to-end demonstration | SYS-01, SYS-02 |

## 8. Known limitations

- Mobile numbers are compared as exact strings. `0412 345 678` and
  `0412345678` are treated as different values. This is a deliberate
  simplification; the demo uses one consistent format.
- Appointments are only for the current day, so no date is stored.
- Slots are fixed at 30 minutes. Custom durations and a generic overlap
  algorithm are not implemented.
- There is no persistence, login, UI, or concurrency handling.
- The demo is a scripted simulation, not an interactive program.
- The verification used focused constructor/value checks and the end-to-end
  console run; no external test framework is part of the submitted project.

## 9. How to reproduce

1. Compile:

   ```bash
   javac -Xlint:all -d out src/AssignmentOne.java src/model/*.java src/service/*.java src/util/*.java
   ```

2. Run:

   ```bash
   java -cp out AssignmentOne
   ```

3. Check the output contains:

   - the professional hierarchy and six professional blocks;
   - type-specific behaviour lines;
   - polymorphic `performRoleDuty()` lines;
   - seven `SUCCESS` booking lines;
   - three booking `WARNING` lines;
   - filtered and sorted appointment lists;
   - two cancellation success messages;
   - three cancellation `WARNING` lines;
   - `Appointments remaining: 4`;
   - `End of demonstration.`
