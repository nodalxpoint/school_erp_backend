package com.schoolerp.school_erp_backend.modules.notice;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;
import com.schoolerp.school_erp_backend.common.filters.SpecificationBuilder;

import com.schoolerp.school_erp_backend.common.filters.FilterUtils;

public class NoticeSpecification {

    private NoticeSpecification() {
    }

    public static Specification<NoticeEntity> filter(NoticeFilterRequest request, UUID schoolId) {

        return new SpecificationBuilder<NoticeEntity>()

                .with(schoolIdEqual(schoolId))

                .with(classIdEqual(request.getClassId()))

                .with(sectionIdEqual(request.getSectionId()))

                .with(targetTypeEqual(request.getTargetType()))

                .with(publishDateBetween(request.getPublishDate()))

                .with(expiryDateBetween(request.getExpiryDate()))

                .with(createdByEqual(request.getCreatedBy()))

                .build();
    }

    public static Specification<NoticeEntity> schoolIdEqual(UUID schoolId) {

        return (root, query, cb) -> FilterUtils.joinEqual(cb, root, "school", "id", schoolId);
    }

    public static Specification<NoticeEntity> classIdEqual(UUID classId) {
        return (root, query, cb) -> FilterUtils.joinEqual(cb, root, "classEntity", "id", classId);
    }

    public static Specification<NoticeEntity> sectionIdEqual(UUID sectionId) {
        return (root, query, cb) -> FilterUtils.joinEqual(cb, root, "sectionEntity", "id", sectionId);
    }

    public static Specification<NoticeEntity> targetTypeEqual(String targetType) {

        return (root, query, cb) -> FilterUtils.equal(cb, root, "targetType", targetType);
    }

    public static Specification<NoticeEntity> publishDateBetween(String dateRange) {

        return (root, query, cb) -> {

            if (dateRange == null)
                return null;

            String[] parts = dateRange.split(" to ");

            if (parts.length == 2) {

                LocalDate start = LocalDate.parse(parts[0].trim());

                LocalDate end = LocalDate.parse(parts[1].trim());

                return cb.between(root.get("publishDate"), start, end);

            }

            return null;
        };
    }

    public static Specification<NoticeEntity> expiryDateBetween(String dateRange) {

        return (root, query, cb) -> {

            if (dateRange == null)
                return null;

            String[] parts = dateRange.split(" to ");

            if (parts.length == 2) {

                LocalDate start = LocalDate.parse(parts[0].trim());

                LocalDate end = LocalDate.parse(parts[1].trim());

                return cb.between(root.get("expiryDate"), start, end);

            }

            return null;
        };
    }

    public static Specification<NoticeEntity> createdByEqual(UUID createdBy) {

        return (root, query, cb) -> FilterUtils.equal(cb, root, "createdBy", createdBy);
    }

}
