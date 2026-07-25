package com.schoolerp.school_erp_backend.modules.feature;

// The full registry of toggleable school modules. `core = true` means every school gets
// it enabled by default (today's behavior, unchanged). New modules added going forward
// should default `core = false` (addon) — a school only gets it once a platform admin
// explicitly turns it on for them.
public enum FeatureKey {
    STUDENTS("Students", true),
    TEACHERS("Teachers", true),
    SUBJECTS("Subjects", true),
    TIMETABLE("Class Timetable", true),
    CLASSES("Classes", true),
    ATTENDANCE("Attendance", true),
    EXAMS("Exams", true),
    FEES("Fees", true),
    UDISE("UDISE Compliance", true),
    REPORTS("Reports", true),
    HOMEWORK("Homework", true),
    NOTICES("Notices", true);

    private final String label;
    private final boolean core;

    FeatureKey(String label, boolean core) {
        this.label = label;
        this.core = core;
    }

    public String getLabel() {
        return label;
    }

    // True = enabled by default for every school. False = addon, disabled until a
    // platform admin turns it on for that specific school.
    public boolean isCore() {
        return core;
    }
}
