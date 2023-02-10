package org.royllo.explorer.core.domain.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.royllo.explorer.core.domain.asset.Asset;
import org.royllo.explorer.core.domain.user.User;
import org.royllo.explorer.core.util.base.BaseDomain;
import org.royllo.explorer.core.util.enums.RequestStatus;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.InheritanceType.JOINED;

/**
 * A request to update royllo data made by a user.
 */
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
@Entity
@Table(name = "REQUESTS")
@Inheritance(strategy = JOINED)
@DiscriminatorColumn(name = "TYPE")
public abstract class Request extends BaseDomain {

    /** Unique identifier. */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    /** Request creator. */
    @NotNull(message = "Request creator is required")
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "FK_USER_CREATOR", updatable = false)
    private User creator;

    /** Request status. */
    @NotNull(message = "Request status is mandatory")
    @Enumerated(STRING)
    @Column(name = "STATUS")
    private RequestStatus status;

    /** The asset created/updated by this request. */
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "FK_ASSET")
    private Asset asset;

    /** Error message - Not empty if status is equals to ERROR. */
    @Column(name = "ERROR_MESSAGE")
    private String errorMessage;

}
