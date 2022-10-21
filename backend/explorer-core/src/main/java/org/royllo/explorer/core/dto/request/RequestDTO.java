package org.royllo.explorer.core.dto.request;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.user.UserDTO;
import org.royllo.explorer.core.util.enums.RequestStatus;

import static org.royllo.explorer.core.util.enums.RequestStatus.FAILURE;
import static org.royllo.explorer.core.util.enums.RequestStatus.SUCCESS;

/**
 * A request to update royllo data made by a user.
 */
@Getter
@SuperBuilder
@ToString
@SuppressWarnings("checkstyle:VisibilityModifier")
public class RequestDTO {

    /** Unique identifier. */
    Long id;

    /** Request creator. */
    UserDTO creator;

    /** Request status. */
    RequestStatus status;

    /** The asset created/updated by this request. */
    AssetDTO asset;

    /** Error message - Not empty if status is equals to ERROR. */
    String errorMessage;

    /**
     * Set the asset created/updated by this request (Cannot be used to update the asset).
     *
     * @param newAsset new asset
     */
    public void setAsset(final AssetDTO newAsset) {
        assert asset == null : "You can't update the asset, it's already set";
        asset = newAsset;
    }

    /**
     * Set the request as success.
     */
    public void succeed() {
        assert asset != null : "Your can't set this status if the asset request is not set";
        status = SUCCESS;
    }

    /**
     * Set the request as failed.
     *
     * @param newErrorMessage new error message
     */
    public void failed(final String newErrorMessage) {
        errorMessage = newErrorMessage;
        status = FAILURE;
    }

}
