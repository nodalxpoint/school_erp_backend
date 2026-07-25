package com.schoolerp.school_erp_backend.modules.feature;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.schoolerp.school_erp_backend.common.exceptions.ResourceNotFoundException;
import com.schoolerp.school_erp_backend.common.exceptions.ValidationException;
import com.schoolerp.school_erp_backend.modules.school.SchoolEntity;
import com.schoolerp.school_erp_backend.modules.school.SchoolRepository;

@Service
public class SchoolFeatureService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SchoolFeatureService.class);

	@Autowired
	private SchoolRepository schoolRepository;

	public List<SchoolFeatureDto> getFeatures(UUID schoolId) {
		SchoolEntity school = findSchool(schoolId);
		Map<FeatureKey, Boolean> overrides = school.getFeatureOverrides();

		return Arrays.stream(FeatureKey.values())
				.map(key -> new SchoolFeatureDto(key.name(), key.getLabel(), !key.isCore(),
						overrides.getOrDefault(key, key.isCore())))
				.toList();
	}

	// Compact key->enabled map, used by the tenant-facing "my effective features" endpoint.
	public Map<String, Boolean> getEffectiveFeatures(UUID schoolId) {
		SchoolEntity school = findSchool(schoolId);
		Map<FeatureKey, Boolean> overrides = school.getFeatureOverrides();

		Map<String, Boolean> result = new LinkedHashMap<>();
		for (FeatureKey key : FeatureKey.values()) {
			result.put(key.name(), overrides.getOrDefault(key, key.isCore()));
		}
		return result;
	}

	@Transactional
	public List<SchoolFeatureDto> updateFeatures(UUID schoolId, Map<String, Boolean> updates,
			UUID actingPlatformAdminUserId) {

		SchoolEntity school = findSchool(schoolId);
		Map<FeatureKey, Boolean> overrides = school.getFeatureOverrides();

		updates.forEach((rawKey, enabled) -> overrides.put(parseKey(rawKey), enabled));

		school.setFeatureOverrides(overrides);
		schoolRepository.save(school);

		LOGGER.info("School features updated: schoolId={} updates={} (by userId={})", schoolId, updates,
				actingPlatformAdminUserId);

		return getFeatures(schoolId);
	}

	private SchoolEntity findSchool(UUID schoolId) {
		return schoolRepository.findById(schoolId)
				.orElseThrow(() -> new ResourceNotFoundException("School not found"));
	}

	private FeatureKey parseKey(String rawKey) {
		try {
			return FeatureKey.valueOf(rawKey);
		} catch (IllegalArgumentException e) {
			throw new ValidationException("Unknown feature key: " + rawKey);
		}
	}
}
