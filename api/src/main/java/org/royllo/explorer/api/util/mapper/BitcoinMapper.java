package org.royllo.explorer.api.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.royllo.explorer.api.domain.bitcoin.TransactionOutput;
import org.royllo.explorer.api.dto.bitcoin.TransactionOutputDTO;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

/**
 * Bitcoin related mapper.
 */
@Mapper(nullValuePropertyMappingStrategy = IGNORE)
public interface BitcoinMapper {

    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    TransactionOutput mapToBitcoinTransactionOutput(TransactionOutputDTO source);

    TransactionOutputDTO mapToBitcoinTransactionOutputDTO(TransactionOutput source);

}
