package org.royllo.explorer.core.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.royllo.explorer.core.domain.request.AddProofRequest;
import org.royllo.explorer.core.domain.request.AddUniverseServerRequest;
import org.royllo.explorer.core.domain.request.Request;
import org.royllo.explorer.core.dto.request.AddProofRequestDTO;
import org.royllo.explorer.core.dto.request.AddUniverseServerRequestDTO;
import org.royllo.explorer.core.dto.request.RequestDTO;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

/**
 * Request related mapper.
 */
@SuppressWarnings("unused")
@Mapper(nullValuePropertyMappingStrategy = IGNORE,
        uses = {UserMapper.class, BitcoinMapper.class, AssetMapper.class, UserMapper.class, UniverseServerMapper.class})
public interface RequestMapper {

    // =================================================================================================================
    // Generic mapper.
    default RequestDTO mapToRequestDTO(Request source) {
        if (source instanceof AddProofRequest) {
            return mapToAddAssetRequestDTO((AddProofRequest) source);
        }
        if (source instanceof AddUniverseServerRequest) {
            return mapToAddUniverseServerRequestDTO((AddUniverseServerRequest) source);
        }
        return null;
    }

    // =================================================================================================================
    // Add asset mapper.
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    AddProofRequest mapToAddAssetRequest(AddProofRequestDTO source);

    AddProofRequestDTO mapToAddAssetRequestDTO(AddProofRequest source);

    // =================================================================================================================
    // Add universe server mapper.
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    AddUniverseServerRequest mapToAddUniverseServerRequest(AddUniverseServerRequestDTO source);

    AddUniverseServerRequestDTO mapToAddUniverseServerRequestDTO(AddUniverseServerRequest source);

}
