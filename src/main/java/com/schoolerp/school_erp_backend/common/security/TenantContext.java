package com.schoolerp.school_erp_backend.common.security;

import java.util.UUID;

/**
 * Holds the current request's tenant (school) id, resolved once per request in
 * {@link JwtAuthenticationFilter} from the authenticated user's own school_id.
 * Must be cleared at the end of every request to avoid leaking a tenant across
 * threads in a pooled servlet container.
 */
public final class TenantContext {

    private static final ThreadLocal<UUID> CURRENT_SCHOOL_ID = new ThreadLocal<>();

    private TenantContext() {
    }

    public static void set(UUID schoolId) {
        CURRENT_SCHOOL_ID.set(schoolId);
    }

    public static UUID get() {
        return CURRENT_SCHOOL_ID.get();
    }

    public static void clear() {
        CURRENT_SCHOOL_ID.remove();
    }
}
