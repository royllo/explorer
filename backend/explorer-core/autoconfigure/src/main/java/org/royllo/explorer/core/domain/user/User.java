package org.royllo.explorer.core.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.royllo.explorer.core.util.base.BaseDomain;
import org.royllo.explorer.core.util.enums.UserRole;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PACKAGE;

/**
 * Application user.
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor(access = PACKAGE)
@Builder
@Entity
@Table(name = "APPLICATION_USER")
public class User extends BaseDomain {

    /** Unique identifier. */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    /** User UUID. */
    @Column(name = "USER_ID", nullable = false, updatable = false)
    private String userId;

    /** User role. */
    @Enumerated(STRING)
    @Column(name = "ROLE", nullable = false)
    private UserRole role;

    /** Username. */
    @Column(name = "USERNAME", nullable = false)
    private String username;

    /** Profile picture file name. */
    @Column(name = "PROFILE_PICTURE_FILE_NAME")
    private String profilePictureFileName;

    /** Full name. */
    @Column(name = "FULL_NAME")
    private String fullName;

    /** Biography. */
    @Column(name = "BIOGRAPHY")
    private String biography;

    /** Website. */
    @Column(name = "WEBSITE")
    private String website;

    /**
     * Setter for username.
     *
     * @param newUsername the username to set
     */
    public void setUsername(final @NonNull String newUsername) {
        username = newUsername.trim().toLowerCase();
    }

}
