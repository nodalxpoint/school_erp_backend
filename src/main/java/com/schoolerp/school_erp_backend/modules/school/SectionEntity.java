package com.schoolerp.school_erp_backend.modules.school;


import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "sections")
public class SectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "class_id", nullable = false)
    private UUID classId;

    @Column(name = "section_name", nullable = false)
    private String sectionName;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getClassId() {
        return classId;
    }

    public void setClassId(UUID classId) {
        this.classId = classId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
