package com.victor.fintechaccountservice.service;

import com.victor.fintechaccountservice.service.dto.AccountOwnerDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.victor.fintechaccountservice.domain.AccountOwner}.
 */
public interface AccountOwnerService {
    /**
     * Save a accountOwner.
     *
     * @param accountOwnerDTO the entity to save.
     * @return the persisted entity.
     */
    AccountOwnerDTO save(AccountOwnerDTO accountOwnerDTO);

    /**
     * Partially updates a accountOwner.
     *
     * @param accountOwnerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AccountOwnerDTO> partialUpdate(AccountOwnerDTO accountOwnerDTO);

    /**
     * Get all the accountOwners.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AccountOwnerDTO> findAll(Pageable pageable);

    /**
     * Get the "id" accountOwner.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AccountOwnerDTO> findOne(Long id);

    /**
     * Delete the "id" accountOwner.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
