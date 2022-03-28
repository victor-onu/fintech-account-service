package com.victor.fintechaccountservice.service.mapper;

import com.victor.fintechaccountservice.domain.FintechAccount;
import com.victor.fintechaccountservice.service.dto.FintechAccountDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FintechAccount} and its DTO {@link FintechAccountDTO}.
 */
@Mapper(componentModel = "spring", uses = { AccountOwnerMapper.class })
public interface FintechAccountMapper extends EntityMapper<FintechAccountDTO, FintechAccount> {
    @Mapping(target = "accountOwner", source = "accountOwner", qualifiedByName = "id")
    FintechAccountDTO toDto(FintechAccount s);
}
