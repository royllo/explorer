package org.royllo.explorer.api.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.royllo.explorer.api.domain.bitcoin.BitcoinTransactionOutput;
import org.royllo.explorer.api.dto.bitcoin.BitcoinTransactionOutputDTO;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

/**
 * Bitcoin related mapper.
 */
@Mapper(nullValuePropertyMappingStrategy = IGNORE)
public interface BitcoinMapper {

    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    BitcoinTransactionOutput mapToBitcoinTransactionOutput(BitcoinTransactionOutputDTO source);

    BitcoinTransactionOutputDTO mapToBitcoinTransactionOutputDTO(BitcoinTransactionOutput source);

}
