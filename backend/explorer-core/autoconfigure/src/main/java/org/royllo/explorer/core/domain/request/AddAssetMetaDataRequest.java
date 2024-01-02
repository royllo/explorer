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
import org.royllo.explorer.core.domain.asset.Asset;

import static jakarta.persistence.FetchType.EAGER;

/**
 * Request to add asset meta data to an asset already in royllo database.
 */
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
@Entity
@Table(name = "REQUEST_ADD_ASSET_META_DATA")
@DiscriminatorValue("ADD_ASSET_META_DATA")
public class AddAssetMetaDataRequest extends Request {

    /** Taproot asset id set by the user. */
    @Column(name = "ASSET_ID", updatable = false)
    private String assetId;

    /** The asset created/updated by this request. */
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "FK_ASSET")
    private Asset asset;

}
