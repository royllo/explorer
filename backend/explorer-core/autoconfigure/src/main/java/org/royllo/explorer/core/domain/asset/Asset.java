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
@Table(name = "ASSET")
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

    /** Asset genesis: The asset ID that uniquely identifies the asset. */
    @Column(name = "ASSET_ID", updatable = false)
    private String assetId;

    /** The asset ID alias in Royllo (generated or configured). */
    @Column(name = "ASSET_ID_ALIAS", nullable = false)
    private String assetIdAlias;

    /** Asset group. */
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "FK_ASSET_GROUP")
    private AssetGroup assetGroup;

    /** Asset genesis: The first outpoint of the transaction that created the asset (txid:vout). */
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "FK_BITCOIN_TRANSACTION_OUTPUT_GENESIS_POINT", updatable = false)
    private BitcoinTransactionOutput genesisPoint;

    /** Asset genesis: The name of the asset. */
    @Column(name = "NAME", updatable = false)
    private String name;

    /** Asset genesis: The hash of the metadata for this genesis asset. */
    @Column(name = "META_DATA_HASH", updatable = false)
    private String metaDataHash;

    /** Meta data file name. */
    @Column(name = "META_DATA_FILE_NAME")
    private String metaDataFileName;

    /** Meta data file mime type. */
    @Column(name = "META_DATA_FILE_MIME_TYPE")
    private String metaDataFileMimeType;

    /** Asset genesis: The index of the output that carries the unique Taproot commitment in the genesis transaction. */
    @Column(name = "OUTPUT_INDEX", updatable = false)
    private int outputIndex;

    /** Asset genesis: The version of the Taproot asset commitment that created this asset. */
    @Column(name = "VERSION", updatable = false)
    private int version;

    /** The type of the asset: normal or a collectible. */
    @Enumerated(STRING)
    @Column(name = "ASSET_TYPE", updatable = false)
    private AssetType type;

    /** The total amount minted for this asset. */
    @Column(name = "AMOUNT")
    private BigInteger amount;

}
