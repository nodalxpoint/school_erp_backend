package com.schoolerp.school_erp_backend.modules.udise;

import java.time.LocalDateTime;
import java.util.UUID;

import com.schoolerp.school_erp_backend.modules.school.SchoolEntity;
import com.schoolerp.school_erp_backend.modules.student.StudentEntity;
import com.schoolerp.school_erp_backend.modules.academic.AcademicSessionEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "student_udise_details")
public class StudentUdiseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    private SchoolEntity school;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_session_id", nullable = false)
    private AcademicSessionEntity academicSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private StudentEntity student;

    @Column(name = "pen", length = 20)
    private String pen;

    @Column(name = "apaar_id", length = 30)
    private String apaarId;

    @Column(name = "aadhaar_last_four", length = 4)
    private String aadhaarLastFour;

    @Column(name = "name_as_per_aadhaar", length = 255)
    private String nameAsPerAadhaar;

    @Column(name = "pincode", length = 20)
    private String pincode;

    @Column(name = "mother_tongue", length = 100)
    private String motherTongue;

    @Column(name = "social_category", length = 50)
    private String socialCategory;

    @Column(name = "minority_group", length = 100)
    private String minorityGroup;

    @Column(name = "bpl_beneficiary")
    private Boolean bplBeneficiary = false;

    @Column(name = "ews_disadvantaged")
    private Boolean ewsDisadvantaged = false;

    @Column(name = "indian_national")
    private Boolean indianNational = true;

    @Column(name = "cwsn")
    private Boolean cwsn = false;

    @Column(name = "disability_type", length = 100)
    private String disabilityType;

    @Column(name = "out_of_school_current_year")
    private Boolean outOfSchoolCurrentYear = false;

    @Column(name = "out_of_school_previous_year")
    private Boolean outOfSchoolPreviousYear = false;

    @Column(name = "udise_status", length = 20)
    private String udiseStatus = "DRAFT";

    @Column(name = "verified_by")
    private UUID verifiedBy;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.bplBeneficiary == null) this.bplBeneficiary = false;
        if (this.ewsDisadvantaged == null) this.ewsDisadvantaged = false;
        if (this.indianNational == null) this.indianNational = true;
        if (this.cwsn == null) this.cwsn = false;
        if (this.outOfSchoolCurrentYear == null) this.outOfSchoolCurrentYear = false;
        if (this.outOfSchoolPreviousYear == null) this.outOfSchoolPreviousYear = false;
        if (this.udiseStatus == null) this.udiseStatus = "DRAFT";
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public SchoolEntity getSchool() {
        return school;
    }

    public void setSchool(SchoolEntity school) {
        this.school = school;
    }

    public AcademicSessionEntity getAcademicSession() {
        return academicSession;
    }

    public void setAcademicSession(AcademicSessionEntity academicSession) {
        this.academicSession = academicSession;
    }

    public StudentEntity getStudent() {
        return student;
    }

    public void setStudent(StudentEntity student) {
        this.student = student;
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
