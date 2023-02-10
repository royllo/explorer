package org.royllo.explorer.core.domain.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.royllo.explorer.core.util.validator.TransactionOutput;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

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
    @TransactionOutput(message = "Genesis point is not a valid txid:vout")
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
    @PositiveOrZero(message = "Valid output index is required")
    @Column(name = "OUTPUT_INDEX", updatable = false)
    private int outputIndex;

    /** Proof that validates the asset information. */
    @NotBlank(message = "Proof is required")
    @Column(name = "PROOF", updatable = false)
    private String proof;

}
