package com.schoolerp.school_erp_backend.modules.teacher;

import java.util.UUID;

public class TeacherClassResponseDto {

    private UUID teacherId;
    private UUID classId;
    private String className;
    private UUID sectionId;
    private String sectionName;

    // exception field
    private String AttendanceCheck;

    public UUID getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(UUID teacherId) {
        this.teacherId = teacherId;
    }

    public UUID getClassId() {
        return classId;
    }

    public void setClassId(UUID classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public UUID getSectionId() {
        return sectionId;
    }

    public void setSectionId(UUID sectionId) {
        this.sectionId = sectionId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getAttendanceCheck() {
        return AttendanceCheck;
    }

    public void setAttendanceCheck(String attendanceCheck) {
        AttendanceCheck = attendanceCheck;
    }

}
