# PROG2004 Assessment 1 — Health Service Appointment System

**Student:** Runze Zhang  
**Student ID:** 25259667  
**Unit:** PROG2004 Object Oriented Programming  
**Assessment:** Assessment 1

## Overview

A small Java appointment system for a health service. The project models health
professionals, patients, appointment slots, and the clinic's booking rules, and
demonstrates the design through a scripted console simulation.

The design covers:

- an abstract `HealthProfessional` base class and four concrete professional
  types;
- encapsulated patient, time-slot, schedule, and appointment objects;
- appointment adding, filtering, sorting, and cancellation;
- validation of IDs, names, working days, slots, and required appointment
  fields;
- polymorphism through `List<HealthProfessional>` and the manager's
  `HealthProfessional` parameters.

## Project structure

```text
src/
├── AssignmentOne.java                 # entry point / demonstration (default package)
├── model/                             # domain model
│   ├── HealthProfessional.java        # abstract base class
│   ├── GeneralPractitioner.java
│   ├── Specialist.java
│   ├── Nurse.java
│   ├── Dietitian.java
│   ├── Patient.java
│   ├── TimeSlot.java
│   ├── ClinicSchedule.java
│   └── Appointment.java
├── service/                           # business logic
│   └── AppointmentManager.java
└── util/                              # shared helpers
    ├── Validation.java
    └── Weekday.java
```

The `model` package contains the objects the system is about. The `service`
package contains the manager that applies booking rules. The `util` package
contains shared validation and the weekday enum. `AssignmentOne` is kept in the
default package so it can be started with `java -cp out AssignmentOne`.

## Documentation

- [Design notes](docs/design-notes.md) — package design, class responsibilities,
  Part 1–6 decisions, OOP mapping, trade-offs, and extension points.
- [Test plan](docs/test-plan.md) — test cases, expected results, traceability,
  and known limitations.

## Assessment mapping

| Part | Implementation |
|---|---|
| Part 1 | `HealthProfessional`, `Weekday`, `Validation` |
| Part 2 | `GeneralPractitioner`, `Specialist`, `Nurse`, `Dietitian` |
| Part 3 | `AssignmentOne` — `List<HealthProfessional>` and polymorphic loops |
| Part 4 | `Patient`, `TimeSlot`, `ClinicSchedule`, `Appointment` |
| Part 5 | `AppointmentManager` |
| Part 6 | `AssignmentOne.demonstrateAppointmentSystem()` |

## Compile and run

```bash
javac -Xlint:all -d out src/AssignmentOne.java src/model/*.java src/service/*.java src/util/*.java
java -cp out AssignmentOne
```

The program prints the health professional hierarchy, polymorphic behaviour,
the appointment workflow, and both normal and error cases.

## Build output

`javac -d out ...` writes the compiled `.class` files to `out/`. The `out/`
directory and `*.class` files are ignored by Git and should not be committed.
Only the `.java` sources belong in the repository.

## Notes

- The program is a console simulation, not a GUI or database application.
- Bookings are for the current day only.
- Clinic slots are fixed at 30 minutes between 08:00 and 16:00.
- The demo uses `Weekday.today()` to include the current weekday in each
  professional's working days, so the printed weekday names can change when the
  program is run on a different day.
