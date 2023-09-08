package org.royllo.explorer.core.domain.universe;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.royllo.explorer.core.domain.user.User;
import org.royllo.explorer.core.util.base.BaseDomain;

import java.time.ZonedDateTime;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * Universe server.
 */
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
@Entity
@Table(name = "UNIVERSE_SERVER")
public class UniverseServer extends BaseDomain {

    /** Unique identifier. */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    /** Universe server UUID. */
    @Column(name = "UNIVERSE_SERVER_ID", updatable = false)
    private String universeServerId;

    /** Universe server owner. */
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "FK_USER_OWNER", nullable = false)
    private User owner;

    /** The universe server address (consists of both an IP address and a port number). */
    @Column(name = "SERVER_ADDRESS", updatable = false)
    private String serverAddress;

    /** When is the last time we contact the server to synchronize. */
    @Column(name = "LAST_SYNCHRONIZED_ON")
    private ZonedDateTime lastSynchronizedOn;

}
