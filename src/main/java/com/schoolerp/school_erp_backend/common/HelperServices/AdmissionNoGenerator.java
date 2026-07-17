package com.schoolerp.school_erp_backend.common.HelperServices;

import java.security.SecureRandom;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.schoolerp.school_erp_backend.modules.student.StudentRepository;

@Component
public class AdmissionNoGenerator {

    @Autowired
    private StudentRepository studentRepository;

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public String generate() {
        int year = LocalDate.now().getYear();
        long count = studentRepository.count() + 1;
        return String.format("ADM-%d-%03d", year, count);
    }

    public String generatePassKey() {
        StringBuilder passKey = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            passKey.append(CHARACTERS.charAt(index));
        }

        return passKey.toString();
    }

}