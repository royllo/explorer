package org.royllo.explorer.core.util.enums;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;

/**
 * User roles.
 */
public enum UserRole {

    /** User. */
    USER,

    /** Administrator. */
    ADMINISTRATOR;

    /**
     * Convert to a {@link SimpleGrantedAuthority}.
     *
     * @return {@link SimpleGrantedAuthority}
     */
    public SimpleGrantedAuthority asGrantedAuthority() {
        return new SimpleGrantedAuthority(this.name());
    }

    /**
     * Convert to a {@link SimpleGrantedAuthority} collection.
     *
     * @return {@link SimpleGrantedAuthority} collection
     */
    public List<SimpleGrantedAuthority> asGrantedAuthorityList() {
        return Collections.singletonList(asGrantedAuthority());
    }

}
