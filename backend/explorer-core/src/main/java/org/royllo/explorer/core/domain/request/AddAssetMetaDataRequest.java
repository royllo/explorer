package org.royllo.explorer.core.domain.request;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Request to add asset meta data to an asset already in royllo database
 */
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
@Entity
@Table(name = "REQUESTS_ADD_ASSET_META_DATA")
@DiscriminatorValue("ADD_ASSET_META_DATA")
public class AddAssetMetaDataRequest extends Request {

    /** Taro asset id. */
    @Column(name = "ASSET_ID", updatable = false)
    private String assetId;

    /** Metadata corresponding to the meta hash stored in the genesis information. */
    @Column(name = "META_DATA", updatable = false)
    private String metaData;

}
