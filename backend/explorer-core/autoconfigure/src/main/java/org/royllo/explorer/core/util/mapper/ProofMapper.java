package org.royllo.explorer.core.util.mapper;

import org.mapstruct.Mapper;
import org.royllo.explorer.core.domain.proof.Proof;
import org.royllo.explorer.core.dto.proof.ProofDTO;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

/**
 * Proof mapper.
 */
@SuppressWarnings("unused")
@Mapper(nullValuePropertyMappingStrategy = IGNORE,
        uses = {UserMapper.class, AssetMapper.class, BitcoinMapper.class})
public interface ProofMapper {

    Proof mapToProof(ProofDTO source);

    ProofDTO mapToProofDTO(Proof source);

}
