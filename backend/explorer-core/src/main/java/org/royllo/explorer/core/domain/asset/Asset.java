package org.royllo.explorer.core.domain.asset;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.royllo.explorer.core.domain.bitcoin.BitcoinTransactionOutput;
import org.royllo.explorer.core.domain.user.User;
import org.royllo.explorer.core.util.base.BaseDomain;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * Taro asset.
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "ASSETS")
public class Asset extends BaseDomain {

    /** Unique identifier. */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    /** The first outpoint of the transaction that created the asset (txid:vout). */
    @NotNull(message = "Bitcoin transaction output is required")
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "FK_BITCOIN_TRANSACTION_OUTPUT_GENESIS_POINT", updatable = false)
    private BitcoinTransactionOutput genesisPoint;

    /** Asset creator. */
    @NotNull(message = "Asset creator is required")
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "FK_USER_CREATOR", nullable = false)
    private User creator;

    /** The name of the asset. */
    @NotBlank(message = "Asset name is required")
    @Column(name = "NAME", updatable = false)
    private String name;

    /** The hashed metadata of the asset. */
    @NotBlank(message = "Hashed metadata is required")
    @Column(name = "META_DATA", updatable = false)
    private String metaData;

    /** The asset ID that uniquely identifies the asset. */
    @NotBlank(message = "Asset ID is required")
    @Column(name = "ASSET_ID", updatable = false)
    private String assetId;

    /** The index of the output that carries the unique Taro commitment in the genesis transaction. */
    @NotNull(message = "Output index is required")
    @Column(name = "OUTPUT_INDEX", updatable = false)
    private int outputIndex;

}
