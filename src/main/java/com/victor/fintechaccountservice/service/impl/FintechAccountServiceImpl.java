package com.victor.fintechaccountservice.service.impl;

import com.victor.fintechaccountservice.domain.FintechAccount;
import com.victor.fintechaccountservice.repository.FintechAccountRepository;
import com.victor.fintechaccountservice.service.FintechAccountService;
import com.victor.fintechaccountservice.service.dto.FintechAccountDTO;
import com.victor.fintechaccountservice.service.mapper.FintechAccountMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FintechAccount}.
 */
@Service
@Transactional
public class FintechAccountServiceImpl implements FintechAccountService {

    private final Logger log = LoggerFactory.getLogger(FintechAccountServiceImpl.class);

    private final FintechAccountRepository fintechAccountRepository;

    private final FintechAccountMapper fintechAccountMapper;

    public FintechAccountServiceImpl(FintechAccountRepository fintechAccountRepository, FintechAccountMapper fintechAccountMapper) {
        this.fintechAccountRepository = fintechAccountRepository;
        this.fintechAccountMapper = fintechAccountMapper;
    }

    @Override
    public FintechAccountDTO save(FintechAccountDTO fintechAccountDTO) {
        log.debug("Request to save FintechAccount : {}", fintechAccountDTO);
        FintechAccount fintechAccount = fintechAccountMapper.toEntity(fintechAccountDTO);
        fintechAccount = fintechAccountRepository.save(fintechAccount);
        return fintechAccountMapper.toDto(fintechAccount);
    }

    @Override
    public Optional<FintechAccountDTO> partialUpdate(FintechAccountDTO fintechAccountDTO) {
        log.debug("Request to partially update FintechAccount : {}", fintechAccountDTO);

        return fintechAccountRepository
            .findById(fintechAccountDTO.getId())
            .map(existingFintechAccount -> {
                fintechAccountMapper.partialUpdate(existingFintechAccount, fintechAccountDTO);

                return existingFintechAccount;
            })
            .map(fintechAccountRepository::save)
            .map(fintechAccountMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FintechAccountDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FintechAccounts");
        return fintechAccountRepository.findAll(pageable).map(fintechAccountMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FintechAccountDTO> findOne(Long id) {
        log.debug("Request to get FintechAccount : {}", id);
        return fintechAccountRepository.findById(id).map(fintechAccountMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FintechAccount : {}", id);
        fintechAccountRepository.deleteById(id);
    }
}
