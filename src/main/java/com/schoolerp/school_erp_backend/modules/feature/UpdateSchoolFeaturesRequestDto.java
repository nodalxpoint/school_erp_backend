package com.schoolerp.school_erp_backend.modules.feature;

import java.util.Map;

import jakarta.validation.constraints.NotEmpty;

public class UpdateSchoolFeaturesRequestDto {

	// Keyed by FeatureKey name (e.g. "ATTENDANCE" -> true). Only the keys you send are
	// changed — anything omitted keeps its current effective value.
	@NotEmpty
	private Map<String, Boolean> features;

	public Map<String, Boolean> getFeatures() {
		return features;
	}

	public void setFeatures(Map<String, Boolean> features) {
		this.features = features;
	}
}
