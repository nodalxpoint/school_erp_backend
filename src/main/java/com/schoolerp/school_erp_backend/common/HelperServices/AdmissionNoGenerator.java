package com.schoolerp.school_erp_backend.common.HelperServices;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.UUID;

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
        String admissionNo;

        do {
            String randomPart = UUID.randomUUID()
                    .toString()
                    .substring(0, 5)
                    .toUpperCase();

            admissionNo = String.format(
                    "ADM-%d-%s",
                    year,
                    randomPart);

        } while (studentRepository.existsByAdmissionNo(admissionNo));

        return admissionNo;
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