package com.victor.fintechaccountservice.service.impl;

import com.victor.fintechaccountservice.domain.AccountOwner;
import com.victor.fintechaccountservice.repository.AccountOwnerRepository;
import com.victor.fintechaccountservice.service.AccountOwnerService;
import com.victor.fintechaccountservice.service.dto.AccountOwnerDTO;
import com.victor.fintechaccountservice.service.fiegn.NotificationService;
import com.victor.fintechaccountservice.service.mapper.AccountOwnerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AccountOwner}.
 */
@Service
@Transactional
public class AccountOwnerServiceImpl implements AccountOwnerService {

    private final Logger log = LoggerFactory.getLogger(AccountOwnerServiceImpl.class);

    private final AccountOwnerRepository accountOwnerRepository;

    private final AccountOwnerMapper accountOwnerMapper;

    private final NotificationService notificationService;

    public AccountOwnerServiceImpl(
        AccountOwnerRepository accountOwnerRepository,
        AccountOwnerMapper accountOwnerMapper,
        NotificationService notificationService
    ) {
        this.accountOwnerRepository = accountOwnerRepository;
        this.accountOwnerMapper = accountOwnerMapper;
        this.notificationService = notificationService;
    }

    @Override
    public AccountOwnerDTO save(AccountOwnerDTO accountOwnerDTO) {
        log.debug("Request to save AccountOwner : {}", accountOwnerDTO);
        AccountOwner accountOwner = accountOwnerMapper.toEntity(accountOwnerDTO);
        accountOwner = accountOwnerRepository.save(accountOwner);
        notificationService.sendCreateAccountMail(accountOwnerDTO);
        return accountOwnerMapper.toDto(accountOwner);
    }

    @Override
    public Optional<AccountOwnerDTO> partialUpdate(AccountOwnerDTO accountOwnerDTO) {
        log.debug("Request to partially update AccountOwner : {}", accountOwnerDTO);

        return accountOwnerRepository
            .findById(accountOwnerDTO.getId())
            .map(existingAccountOwner -> {
                accountOwnerMapper.partialUpdate(existingAccountOwner, accountOwnerDTO);

                return existingAccountOwner;
            })
            .map(accountOwnerRepository::save)
            .map(accountOwnerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountOwnerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AccountOwners");
        return accountOwnerRepository.findAll(pageable).map(accountOwnerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccountOwnerDTO> findOne(Long id) {
        log.debug("Request to get AccountOwner : {}", id);
        return accountOwnerRepository.findById(id).map(accountOwnerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AccountOwner : {}", id);
        accountOwnerRepository.deleteById(id);
    }
}
