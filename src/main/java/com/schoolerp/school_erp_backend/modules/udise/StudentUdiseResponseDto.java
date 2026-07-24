package com.schoolerp.school_erp_backend.modules.udise;

import java.time.LocalDateTime;
import java.util.UUID;

public class StudentUdiseResponseDto {

    private UUID id;
    private UUID schoolId;
    private String schoolName;
    private UUID academicSessionId;
    private String academicSessionName;
    private UUID studentId;
    private String studentName;

    private String pen;
    private String apaarId;
    private String aadhaarLastFour;
    private String nameAsPerAadhaar;
    private String pincode;

    private String motherTongue;
    private String socialCategory;
    private String minorityGroup;

    private Boolean bplBeneficiary;
    private Boolean ewsDisadvantaged;
    private Boolean indianNational;

    private Boolean cwsn;
    private String disabilityType;

    private Boolean outOfSchoolCurrentYear;
    private Boolean outOfSchoolPreviousYear;

    private String udiseStatus;
    private UUID verifiedBy;
    private LocalDateTime verifiedAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(UUID schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public UUID getAcademicSessionId() {
        return academicSessionId;
    }

    public void setAcademicSessionId(UUID academicSessionId) {
        this.academicSessionId = academicSessionId;
    }

    public String getAcademicSessionName() {
        return academicSessionName;
    }

    public void setAcademicSessionName(String academicSessionName) {
        this.academicSessionName = academicSessionName;
    }

    public UUID getStudentId() {
        return studentId;
    }

    public void setStudentId(UUID studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getPen() {
        return pen;
    }

    public void setPen(String pen) {
        this.pen = pen;
    }

    public String getApaarId() {
        return apaarId;
    }

    public void setApaarId(String apaarId) {
        this.apaarId = apaarId;
    }

    public String getAadhaarLastFour() {
        return aadhaarLastFour;
    }

    public void setAadhaarLastFour(String aadhaarLastFour) {
        this.aadhaarLastFour = aadhaarLastFour;
    }

    public String getNameAsPerAadhaar() {
        return nameAsPerAadhaar;
    }

    public void setNameAsPerAadhaar(String nameAsPerAadhaar) {
        this.nameAsPerAadhaar = nameAsPerAadhaar;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getMotherTongue() {
        return motherTongue;
    }

    public void setMotherTongue(String motherTongue) {
        this.motherTongue = motherTongue;
    }

    public String getSocialCategory() {
        return socialCategory;
    }

    public void setSocialCategory(String socialCategory) {
        this.socialCategory = socialCategory;
    }

    public String getMinorityGroup() {
        return minorityGroup;
    }

    public void setMinorityGroup(String minorityGroup) {
        this.minorityGroup = minorityGroup;
    }

    public Boolean getBplBeneficiary() {
        return bplBeneficiary;
    }

    public void setBplBeneficiary(Boolean bplBeneficiary) {
        this.bplBeneficiary = bplBeneficiary;
    }

    public Boolean getEwsDisadvantaged() {
        return ewsDisadvantaged;
    }

    public void setEwsDisadvantaged(Boolean ewsDisadvantaged) {
        this.ewsDisadvantaged = ewsDisadvantaged;
    }

    public Boolean getIndianNational() {
        return indianNational;
    }

    public void setIndianNational(Boolean indianNational) {
        this.indianNational = indianNational;
    }

    public Boolean getCwsn() {
        return cwsn;
    }

    public void setCwsn(Boolean cwsn) {
        this.cwsn = cwsn;
    }

    public String getDisabilityType() {
        return disabilityType;
    }

    public void setDisabilityType(String disabilityType) {
        this.disabilityType = disabilityType;
    }

    public Boolean getOutOfSchoolCurrentYear() {
        return outOfSchoolCurrentYear;
    }

    public void setOutOfSchoolCurrentYear(Boolean outOfSchoolCurrentYear) {
        this.outOfSchoolCurrentYear = outOfSchoolCurrentYear;
    }

    public Boolean getOutOfSchoolPreviousYear() {
        return outOfSchoolPreviousYear;
    }

    public void setOutOfSchoolPreviousYear(Boolean outOfSchoolPreviousYear) {
        this.outOfSchoolPreviousYear = outOfSchoolPreviousYear;
    }

    public String getUdiseStatus() {
        return udiseStatus;
    }

    public void setUdiseStatus(String udiseStatus) {
        this.udiseStatus = udiseStatus;
    }

    public UUID getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(UUID verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public LocalDateTime getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(LocalDateTime verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
