package org.royllo.explorer.core.domain.request;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.royllo.explorer.core.domain.universe.UniverseServer;

import static jakarta.persistence.FetchType.EAGER;

/**
 * Request to add a universe server to royllo database.
 */
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
@Entity
@Table(name = "REQUEST_ADD_UNIVERSE_SERVER")
@DiscriminatorValue("ADD_UNIVERSE_SERVER")
public class AddUniverseServerRequest extends Request {

    /** The universe server address (consists of both an IP address and a port number). */
    @Column(name = "SERVER_ADDRESS", updatable = false)
    private String serverAddress;

    /** The universe server created/updated by this request. */
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "FK_UNIVERSE_SERVER")
    private UniverseServer universeServer;

}
