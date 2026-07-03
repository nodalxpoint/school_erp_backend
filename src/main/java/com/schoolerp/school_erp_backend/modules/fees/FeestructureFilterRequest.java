package com.schoolerp.school_erp_backend.modules.fees;

import java.util.UUID;
import com.schoolerp.school_erp_backend.common.filters.BaseFilterRequest;

public class FeestructureFilterRequest extends BaseFilterRequest {

    private UUID schoolId;
    private UUID classId;
    private String feeName;
    private String frequency;
    private String search;
    private UUID studentId;

    public UUID getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(UUID schoolId) {
        this.schoolId = schoolId;
    }

    public UUID getClassId() {
        return classId;
    }

    public void setClassId(UUID classId) {
        this.classId = classId;
    }

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public UUID getStudentId() {
        return studentId;
    }

    public void setStudentId(UUID studentId) {
        this.studentId = studentId;
    }
}
