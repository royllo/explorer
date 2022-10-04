package org.royllo.explorer.api.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.royllo.explorer.api.util.base.BaseDomain;
import org.royllo.explorer.api.util.enums.UserRole;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

/**
 * Application user.
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "USERS")
public class User extends BaseDomain {

    /** Unique identifier. */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    /** Username. */
    @NotBlank(message = "Username is mandatory")
    @Column(name = "USERNAME", nullable = false)
    private String username;

    /** User role. */
    @NotNull(message = "Role is mandatory")
    @Enumerated(STRING)
    @Column(name = "ROLE")
    private UserRole role;

}
