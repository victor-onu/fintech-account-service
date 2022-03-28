package com.victor.fintechaccountservice.service;

import com.victor.fintechaccountservice.service.dto.FintechAccountDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.victor.fintechaccountservice.domain.FintechAccount}.
 */
public interface FintechAccountService {
    /**
     * Save a fintechAccount.
     *
     * @param fintechAccountDTO the entity to save.
     * @return the persisted entity.
     */
    FintechAccountDTO save(FintechAccountDTO fintechAccountDTO);

    /**
     * Partially updates a fintechAccount.
     *
     * @param fintechAccountDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FintechAccountDTO> partialUpdate(FintechAccountDTO fintechAccountDTO);

    /**
     * Get all the fintechAccounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FintechAccountDTO> findAll(Pageable pageable);

    /**
     * Get the "id" fintechAccount.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FintechAccountDTO> findOne(Long id);

    /**
     * Delete the "id" fintechAccount.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
