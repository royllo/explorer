package org.royllo.explorer.core.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.royllo.explorer.core.domain.proof.Proof;
import org.royllo.explorer.core.dto.proof.ProofDTO;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

/**
 * Proof related mapper.
 */
@Mapper(nullValuePropertyMappingStrategy = IGNORE, uses = {UserMapper.class})
public interface ProofMapper {

    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    Proof mapToProof(ProofDTO source);

    ProofDTO mapToProofDTO(Proof source);

}
