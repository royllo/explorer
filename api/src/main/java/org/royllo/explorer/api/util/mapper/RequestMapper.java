package org.royllo.explorer.api.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.royllo.explorer.api.domain.request.AddAssetMetaDataRequest;
import org.royllo.explorer.api.domain.request.AddAssetRequest;
import org.royllo.explorer.api.domain.request.Request;
import org.royllo.explorer.api.dto.request.AddAssetMetaDataRequestDTO;
import org.royllo.explorer.api.dto.request.AddAssetRequestDTO;
import org.royllo.explorer.api.dto.request.RequestDTO;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

/**
 * Request related mapper.
 */
@Mapper(nullValuePropertyMappingStrategy = IGNORE, uses = {UserMapper.class})
public interface RequestMapper {

    // =================================================================================================================
    // Generic mapper.
    default RequestDTO mapToRequestDTO(Request source) {
        // TODO Simplify when pattern Matching for switch will be available.
        if (source instanceof AddAssetRequest) {
            return mapToAddAssetRequestDTO((AddAssetRequest) source);
        }
        if (source instanceof AddAssetMetaDataRequest) {
            return mapToAddAssetMetaRequestDTO((AddAssetMetaDataRequest) source);
        }
        return null;
    }

    // =================================================================================================================
    // Add asset Mapper.
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    AddAssetRequest mapToAddAssetRequest(AddAssetRequestDTO source);

    AddAssetRequestDTO mapToAddAssetRequestDTO(AddAssetRequest source);

    // =================================================================================================================
    // Add asset meta Mapper.
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    AddAssetMetaDataRequest mapToAddAssetMetaRequest(AddAssetMetaDataRequestDTO source);

    AddAssetMetaDataRequestDTO mapToAddAssetMetaRequestDTO(AddAssetMetaDataRequest source);

}
