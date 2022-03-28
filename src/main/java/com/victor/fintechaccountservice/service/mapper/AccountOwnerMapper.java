package com.victor.fintechaccountservice.service.mapper;

import com.victor.fintechaccountservice.domain.AccountOwner;
import com.victor.fintechaccountservice.service.dto.AccountOwnerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AccountOwner} and its DTO {@link AccountOwnerDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AccountOwnerMapper extends EntityMapper<AccountOwnerDTO, AccountOwner> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AccountOwnerDTO toDtoId(AccountOwner accountOwner);
}
