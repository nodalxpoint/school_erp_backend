package com.schoolerp.school_erp_backend.modules.superAdmin;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.schoolerp.school_erp_backend.common.HelperServices.AdmissionNoGenerator;
import com.schoolerp.school_erp_backend.common.exceptions.ResourceNotFoundException;
import com.schoolerp.school_erp_backend.common.exceptions.ValidationException;
import com.schoolerp.school_erp_backend.common.response.PagedResponse;
import com.schoolerp.school_erp_backend.modules.auth.User;
import com.schoolerp.school_erp_backend.modules.auth.UserRepository;
import com.schoolerp.school_erp_backend.modules.auth.UserRole;
import com.schoolerp.school_erp_backend.modules.school.SchoolEntity;

@Service
public class SuperAdminService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SuperAdminService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdmissionNoGenerator admissionNoGenerator;

    @Transactional
    public void createOrUpdateSchoolAdmin(CreateSchoolAdminRequestDto dto, UUID superAdminUserId) {
        // Resolve target role: default to SCHOOL_ADMIN if not provided
        UserRole targetRole = (dto.getRole() != null) ? dto.getRole() : UserRole.SCHOOL_ADMIN;

        if (targetRole != UserRole.SCHOOL_ADMIN && targetRole != UserRole.ACCOUNTANT) {
            throw new ValidationException(
                    "Invalid role. Only SCHOOL_ADMIN and ACCOUNTANT accounts can be created or updated.");
        }

        // Resolve Super Admin and their associated School
        User superAdmin = userRepository.findById(superAdminUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Super admin not found with ID: " + superAdminUserId));

        SchoolEntity school = superAdmin.getSchool();
        if (school == null) {
            throw new ValidationException("Super admin is not associated with any school");
        }
        if (Boolean.TRUE.equals(school.getIsDeleted())) {
            throw new ValidationException("Cannot assign admin to a deleted school");
        }

        if (dto.getId() != null) {
            LOGGER.debug("Updating existing school admin/accountant: {}", dto.getId());
            updateSchoolAdmin(dto, school, targetRole);
        } else {
            LOGGER.debug("Creating new school admin/accountant");
            createSchoolAdmin(dto, school, targetRole);
        }
    }

    public PagedResponse<AdminResponseDto> getAdminsAndAccountants(AdminFilterRequest request, UUID superAdminUserId) {
        LOGGER.debug("Filtering admins and accountants");

        // Resolve Super Admin and their associated School
        User superAdmin = userRepository.findById(superAdminUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Super admin not found with ID: " + superAdminUserId));

        SchoolEntity school = superAdmin.getSchool();
        if (school == null) {
            throw new ValidationException("Super admin is not associated with any school");
        }

        Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Page<User> userPage = userRepository.findAll(AdminSpecification.filter(request, school.getId()), pageable);

        Page<AdminResponseDto> dtoPage = userPage.map(user -> {
            AdminResponseDto dto = new AdminResponseDto();
            dto.setId(user.getId());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setEmail(user.getEmail());
            dto.setRole(user.getRole());
            dto.setPhoneNumber(user.getPhoneNumber());
            dto.setIsActive(user.getIsActive());
            dto.setPassKey(user.getPassKey());
            return dto;
        });

        return PagedResponse.fromPage(dtoPage, "Admins and Accountants fetched successfully");
    }

    // ─── UPDATE ────────────────────────────────────────────────
    private void updateSchoolAdmin(CreateSchoolAdminRequestDto dto, SchoolEntity school, UserRole targetRole) {
        User admin = userRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + dto.getId()));

        // Ensure the user being updated is a school admin or accountant
        if (admin.getRole() != UserRole.SCHOOL_ADMIN && admin.getRole() != UserRole.ACCOUNTANT) {
            throw new ValidationException("User is not a school admin or accountant");
        }

        // Bind to the Super Admin's school
        admin.setSchool(school);

        // Update role
        admin.setRole(targetRole);

        // Update basic fields
        if (dto.getAdminFirstName() != null && !dto.getAdminFirstName().trim().isEmpty()) {
            admin.setFirstName(dto.getAdminFirstName().trim());
        }
        if (dto.getAdminLastName() != null) {
            admin.setLastName(dto.getAdminLastName().trim());
        }

        if (dto.getAdminEmail() != null && !dto.getAdminEmail().trim().isEmpty()) {
            String normalizedEmail = dto.getAdminEmail().trim().toLowerCase();
            if (!normalizedEmail.equals(admin.getEmail()) && userRepository.existsByEmail(normalizedEmail)) {
                throw new ValidationException("Admin email is already registered");
            }
            admin.setEmail(normalizedEmail);
        }

        if (dto.getAdminPhone() != null) {
            String cleanPhone = dto.getAdminPhone().trim();
            if (!cleanPhone.equals(admin.getPhoneNumber()) && !cleanPhone.isEmpty()
                    && userRepository.existsByPhoneNumber(cleanPhone)) {
                throw new ValidationException("Admin phone number is already registered");
            }
            admin.setPhoneNumber(cleanPhone.isEmpty() ? null : cleanPhone);
        }

        if (dto.getAdminPassword() != null && !dto.getAdminPassword().trim().isEmpty()) {
            if (dto.getAdminPassword().trim().length() < 6) {
                throw new ValidationException("Password must be at least 6 characters");
            }
            admin.setPassword(passwordEncoder.encode(dto.getAdminPassword()));
        }

        userRepository.save(admin);
    }

    // ─── CREATE ────────────────────────────────────────────────
    private void createSchoolAdmin(CreateSchoolAdminRequestDto dto, SchoolEntity school, UserRole targetRole) {
        if (dto.getAdminEmail() == null || dto.getAdminEmail().trim().isEmpty()) {
            throw new ValidationException("Admin email is required");
        }
        String normalizedEmail = dto.getAdminEmail().trim().toLowerCase();
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new ValidationException("Admin email is already registered");
        }

        if (dto.getAdminPhone() != null && !dto.getAdminPhone().trim().isEmpty()) {
            String cleanPhone = dto.getAdminPhone().trim();
            if (userRepository.existsByPhoneNumber(cleanPhone)) {
                throw new ValidationException("Admin phone number is already registered");
            }
        }

        if (dto.getAdminPassword() == null || dto.getAdminPassword().trim().isEmpty()) {
            throw new ValidationException("Admin password is required");
        }
        if (dto.getAdminPassword().trim().length() < 6) {
            throw new ValidationException("Password must be at least 6 characters");
        }

        User admin = new User();
        admin.setSchool(school);
        admin.setFirstName(dto.getAdminFirstName().trim());
        admin.setLastName(dto.getAdminLastName() != null ? dto.getAdminLastName().trim() : null);
        admin.setEmail(normalizedEmail);
        admin.setPassword(passwordEncoder.encode(dto.getAdminPassword()));
        admin.setRole(targetRole);
        admin.setIsActive(true);
        admin.setPhoneNumber(dto.getAdminPhone() != null ? dto.getAdminPhone().trim() : null);
        admin.setPassKey(admissionNoGenerator.generatePassKey());

        userRepository.save(admin);
    }

    @Transactional
    public void deleteSchoolAdminOrAccountant(UUID targetUserId, UUID superAdminUserId) {
        // Resolve Super Admin and their associated School
        User superAdmin = userRepository.findById(superAdminUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Super admin not found with ID: " + superAdminUserId));

        SchoolEntity school = superAdmin.getSchool();
        if (school == null) {
            throw new ValidationException("Super admin is not associated with any school");
        }

        // Resolve target user
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + targetUserId));

        // Validate target user belongs to the same school as the super admin
        if (targetUser.getSchool() == null || !school.getId().equals(targetUser.getSchool().getId())) {
            throw new ValidationException("User does not belong to the same school");
        }

        // Validate target user role is either SCHOOL_ADMIN or ACCOUNTANT
        if (targetUser.getRole() != UserRole.SCHOOL_ADMIN && targetUser.getRole() != UserRole.ACCOUNTANT) {
            throw new ValidationException("Only school admin or accountant accounts can be deleted.");
        }

        LOGGER.info("Deleting school admin/accountant: {}", targetUserId);
        userRepository.delete(targetUser);
    }
}
