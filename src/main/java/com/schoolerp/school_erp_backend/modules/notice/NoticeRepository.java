package com.schoolerp.school_erp_backend.modules.notice;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<NoticeEntity, UUID>, JpaSpecificationExecutor<NoticeEntity> {

    Page<NoticeEntity> findAll(Specification<NoticeEntity> spec, Pageable pageable);

}
