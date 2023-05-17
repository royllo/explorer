package org.royllo.explorer.core.domain.asset;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.royllo.explorer.core.domain.bitcoin.BitcoinTransactionOutput;
import org.royllo.explorer.core.domain.user.User;
import org.royllo.explorer.core.util.base.BaseDomain;
import org.royllo.explorer.core.util.enums.AssetType;

import java.math.BigInteger;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * Taproot asset.
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

    /** Asset creator. */
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "FK_USER_CREATOR", nullable = false)
    private User creator;

    /** The version of the Taproot asset. */
    @Column(name = "VERSION", updatable = false)
    private int version;

    /** The first outpoint of the transaction that created the asset (txid:vout). */
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "FK_BITCOIN_TRANSACTION_OUTPUT_GENESIS_POINT", updatable = false)
    private BitcoinTransactionOutput genesisPoint;

    /** The name of the asset. */
    @Column(name = "NAME", updatable = false)
    private String name;

    /** The hashed metadata of the asset. */
    @Column(name = "META_DATA", updatable = false)
    private String metaData;

    /** The asset ID that uniquely identifies the asset. */
    @Column(name = "ASSET_ID", updatable = false)
    private String assetId;

    /** The index of the output that carries the unique Taproot commitment in the genesis transaction. */
    @Column(name = "OUTPUT_INDEX", updatable = false)
    private int outputIndex;

    /** The full genesis information encoded in a portable manner, so it can be easily copy/pasted for address creation. */
    @Column(name = "GENESIS_BOOTSTRAP_INFORMATION", updatable = false)
    private String genesisBootstrapInformation;

    /** The version of the Taproot asset commitment that created this asset. */
    @Column(name = "GENESIS_VERSION", updatable = false)
    private int genesisVersion;

    /** The type of the asset: normal or a collectible. */
    @Enumerated(STRING)
    @Column(name = "ASSET_TYPE", updatable = false)
    private AssetType type;

    /** The total amount of the asset stored in this Taproot asset UTXO. */
    @Column(name = "AMOUNT")
    private BigInteger amount;

    /** An optional lock time, as with Bitcoin transactions. */
    @Column(name = "LOCK_TIME")
    private int lockTime;

    /** An optional relative lock time, as with Bitcoin transactions. */
    @Column(name = "RELATIVE_LOCK_TIME")
    private int relativeLockTime;

    /** The version of the script, only version 0 is defined at present. */
    @Column(name = "SCRIPT_VERSION")
    private int scriptVersion;

    /** The script key of the asset, which can be spent under Taproot semantics. */
    @Column(name = "SCRIPT_KEY")
    private String scriptKey;

    /** The transaction that anchors the Taproot asset commitment where the asset resides. */
    @Column(name = "ANCHOR_TX")
    private String anchorTx;

    /** The txid of the anchor transaction. */
    @Column(name = "ANCHOR_TXID")
    private String anchorTxId;

    /** The block hash the contains the anchor transaction above. */
    @Column(name = "ANCHOR_BLOCK_HASH")
    private String anchorBlockHash;

    /** Outpoint (txid:vout) that stores the Taproot asset commitment. */
    @Column(name = "ANCHOR_OUTPOINT")
    private String anchorOutpoint;

    /** The raw internal key that was used to create the anchor Taproot output key. */
    @Column(name = "ANCHOR_INTERNAL_KEY")
    private String anchorInternalKey;

}
