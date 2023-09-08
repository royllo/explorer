package org.royllo.explorer.core.util.mapper;

import org.mapstruct.Mapper;
import org.royllo.explorer.core.domain.proof.ProofFile;
import org.royllo.explorer.core.dto.proof.ProofFileDTO;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

/**
 * Proof file related mapper.
 */
@SuppressWarnings("unused")
@Mapper(nullValuePropertyMappingStrategy = IGNORE, uses = {UserMapper.class, AssetMapper.class, BitcoinMapper.class})
public interface ProofFileMapper {

    ProofFile mapToProofFile(ProofFileDTO source);

    ProofFileDTO mapToProofFileDTO(ProofFile source);

}
