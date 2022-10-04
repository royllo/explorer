package org.royllo.explorer.api.domain.universe;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.royllo.explorer.api.domain.user.User;
import org.royllo.explorer.api.util.base.BaseDomain;
import org.royllo.explorer.api.util.enums.UpdateRequestStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

/**
 * Universe update.
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "UNIVERSE_UPDATES")
public class Update extends BaseDomain {

    /** Unique identifier. */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    /** The user who created this universe update. */
    @NotNull(message = "Creator is mandatory")
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "FK_CREATOR_USER_ID", updatable = false)
    private User creator;

    /** Universe update request status. */
    @NotNull(message = "Status is mandatory")
    @Enumerated(STRING)
    @Column(name = "STATUS")
    private UpdateRequestStatus status;

    // TODO Replace txId/vout with a reference to TransactionOutput.

    /** Transaction id. */
    @Column(name = "TXID")
    private String txId;

    /** Output index (starts at 0). */
    @Column(name = "VOUT")
    private int vout;

    /** Indicates in which status the error occurred. */
    @Enumerated(STRING)
    @Column(name = "FAILURE_STATUS")
    private UpdateRequestStatus failureStatus;

    /** Failure message. */
    @Column(name = "FAILURE_MESSAGE")
    private String failureMessage;

}
