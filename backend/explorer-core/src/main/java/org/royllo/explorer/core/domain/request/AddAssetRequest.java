package org.royllo.explorer.core.domain.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * A request to add asset to royllo explorer.
 */
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
@Entity
@Table(name = "REQUESTS_ADD_ASSET")
@DiscriminatorValue("ADD_ASSET")
public class AddAssetRequest extends Request {

    /** The first outpoint of the transaction that created the asset (txid:vout). */
    @NotBlank(message = "Transaction output that created the asset")
    @Column(name = "GENESIS_POINT", updatable = false)
    private String genesisPoint;

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

    /** Proof that validates the asset information. */
    @NotBlank(message = "Proof is required")
    @Column(name = "PROOF", updatable = false)
    private String proof;

}
