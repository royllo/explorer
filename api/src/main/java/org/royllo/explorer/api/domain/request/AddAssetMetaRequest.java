package org.royllo.explorer.api.domain.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * A request to add asset meta data to an asset already in Royllo database.
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "REQUESTS_ADD_ASSET_META_DATA")
@DiscriminatorValue("ADD_ASSET_META_DATA")
public class AddAssetMetaRequest extends Request {

    /** Taro asset id. */
    @NotBlank(message = "Taro asset id is required")
    @Column(name = "TARO_ASSET_ID", updatable = false)
    private String taroAssetId;

    /** Metadata corresponding to the meta hash stored in the genesis information. */
    @NotBlank(message = "Meta is required")
    @Column(name = "META", updatable = false)
    private String meta;

}
