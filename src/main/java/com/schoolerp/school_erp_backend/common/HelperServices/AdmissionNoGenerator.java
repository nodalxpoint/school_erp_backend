package com.schoolerp.school_erp_backend.common.HelperServices;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.schoolerp.school_erp_backend.modules.student.StudentRepository;

@Component
public class AdmissionNoGenerator {

    @Autowired
    private StudentRepository studentRepository;

    public String generate() {
        int year = LocalDate.now().getYear();
        long count = studentRepository.count() + 1;
        return String.format("ADM-%d-%03d", year, count);
    }
}