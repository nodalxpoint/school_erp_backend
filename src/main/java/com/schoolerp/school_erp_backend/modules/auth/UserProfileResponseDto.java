package com.schoolerp.school_erp_backend.modules.auth;

import java.time.LocalDate;
import java.util.UUID;

public class UserProfileResponseDto {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String role;
    private UUID schoolId;
    private String schoolName;
    private String passKey;

    private TeacherProfileDetails teacherDetails;
    private ParentProfileDetails parentDetails;

    public static class TeacherProfileDetails {
        private UUID teacherId;
        private String employeeCode;
        private String qualification;
        private LocalDate joiningDate;
        private String teacherPassKey;

        public UUID getTeacherId() {
            return teacherId;
        }

        public void setTeacherId(UUID teacherId) {
            this.teacherId = teacherId;
        }

        public String getEmployeeCode() {
            return employeeCode;
        }

        public void setEmployeeCode(String employeeCode) {
            this.employeeCode = employeeCode;
        }

        public String getQualification() {
            return qualification;
        }

        public void setQualification(String qualification) {
            this.qualification = qualification;
        }

        public LocalDate getJoiningDate() {
            return joiningDate;
        }

        public void setJoiningDate(LocalDate joiningDate) {
            this.joiningDate = joiningDate;
        }

        public String getTeacherPassKey() {
            return teacherPassKey;
        }

        public void setTeacherPassKey(String teacherPassKey) {
            this.teacherPassKey = teacherPassKey;
        }
    }

    public static class ParentProfileDetails {
        private UUID parentId;
        private String fatherName;
        private String motherName;
        private String emergencyContact;
        private String parentPassKey;

        public UUID getParentId() {
            return parentId;
        }

        public void setParentId(UUID parentId) {
            this.parentId = parentId;
        }

        public String getFatherName() {
            return fatherName;
        }

        public void setFatherName(String fatherName) {
            this.fatherName = fatherName;
        }

        public String getMotherName() {
            return motherName;
        }

        public void setMotherName(String motherName) {
            this.motherName = motherName;
        }

        public String getEmergencyContact() {
            return emergencyContact;
        }

        public void setEmergencyContact(String emergencyContact) {
            this.emergencyContact = emergencyContact;
        }

        public String getParentPassKey() {
            return parentPassKey;
        }

        public void setParentPassKey(String parentPassKey) {
            this.parentPassKey = parentPassKey;
        }
    }

    // Getters and Setters for top-level fields
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public TeacherProfileDetails getTeacherDetails() {
        return teacherDetails;
    }

    public void setTeacherDetails(TeacherProfileDetails teacherDetails) {
        this.teacherDetails = teacherDetails;
    }

    public ParentProfileDetails getParentDetails() {
        return parentDetails;
    }

    public void setParentDetails(ParentProfileDetails parentDetails) {
        this.parentDetails = parentDetails;
    }

    public String getPassKey() {
        return passKey;
    }

    public void setPassKey(String passKey) {
        this.passKey = passKey;
    }

}
