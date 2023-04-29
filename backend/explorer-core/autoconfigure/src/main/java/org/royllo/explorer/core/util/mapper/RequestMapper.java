package org.royllo.explorer.core.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.royllo.explorer.core.domain.request.AddAssetMetaDataRequest;
import org.royllo.explorer.core.domain.request.AddProof;
import org.royllo.explorer.core.domain.request.Request;
import org.royllo.explorer.core.dto.request.AddAssetMetaDataRequestDTO;
import org.royllo.explorer.core.dto.request.AddProofRequestDTO;
import org.royllo.explorer.core.dto.request.RequestDTO;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

/**
 * Request related mapper.
 */
@Mapper(nullValuePropertyMappingStrategy = IGNORE, uses = {UserMapper.class, BitcoinMapper.class, AssetMapper.class})
public interface RequestMapper {

    // =================================================================================================================
    // Generic mapper.
    default RequestDTO mapToRequestDTO(Request source) {
        if (source instanceof AddProof) {
            return mapToAddAssetRequestDTO((AddProof) source);
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
    AddProof mapToAddAssetRequest(AddProofRequestDTO source);

    AddProofRequestDTO mapToAddAssetRequestDTO(AddProof source);

    // =================================================================================================================
    // Add asset meta Mapper.
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    AddAssetMetaDataRequest mapToAddAssetMetaRequest(AddAssetMetaDataRequestDTO source);

    AddAssetMetaDataRequestDTO mapToAddAssetMetaRequestDTO(AddAssetMetaDataRequest source);

}
