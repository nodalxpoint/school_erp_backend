package com.schoolerp.school_erp_backend.modules.school;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
 
import com.schoolerp.school_erp_backend.common.exceptions.ResourceNotFoundException;
 
@Service
public class SchoolService {
	  @Autowired
	    private SectionRepository sectionRepository;

	    public void createSection(SectionDto request) {

	        SectionEntity section = new SectionEntity();

	        section.setClassId(request.getClassId());
	        section.setSectionName(request.getSectionName());

	        sectionRepository.save(section);
	    }

	    public Page<SectionDto> getAllSections(Pageable pageable) {

	        Page<SectionEntity> sectionsPage = sectionRepository.findAll(pageable);

	        return sectionsPage.map(section ->
	                new SectionDto(
	                        section.getId(),
	                        section.getSectionName(),
	                        section.getClassId()
	                )
	        );
	    }
	}