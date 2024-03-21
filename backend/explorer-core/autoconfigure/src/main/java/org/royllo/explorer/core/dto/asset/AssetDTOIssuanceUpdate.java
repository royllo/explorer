package org.royllo.explorer.core.dto.asset;

import lombok.Builder;

import java.math.BigInteger;
import java.time.ZonedDateTime;

/**
 * Asset issuance data update.
 *
 * @param metadata     The asset metadata
 * @param amount       The asset amount issued
 * @param issuanceDate The asset issuance date
 */
@Builder
public record AssetDTOIssuanceUpdate(
        String metadata,
        BigInteger amount,
        ZonedDateTime issuanceDate) {
}
