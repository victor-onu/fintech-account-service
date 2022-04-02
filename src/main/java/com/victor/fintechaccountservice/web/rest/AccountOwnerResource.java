package com.victor.fintechaccountservice.web.rest;

import com.victor.fintechaccountservice.repository.AccountOwnerRepository;
import com.victor.fintechaccountservice.service.AccountOwnerService;
import com.victor.fintechaccountservice.service.dto.AccountOwnerDTO;
import com.victor.fintechaccountservice.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.victor.fintechaccountservice.domain.AccountOwner}.
 */
@RestController
@RequestMapping("/api")
public class AccountOwnerResource {

    private final Logger log = LoggerFactory.getLogger(AccountOwnerResource.class);

    private static final String ENTITY_NAME = "fintechAccountServiceAccountOwner";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccountOwnerService accountOwnerService;

    private final AccountOwnerRepository accountOwnerRepository;

    public AccountOwnerResource(AccountOwnerService accountOwnerService, AccountOwnerRepository accountOwnerRepository) {
        this.accountOwnerService = accountOwnerService;
        this.accountOwnerRepository = accountOwnerRepository;
    }

    /**
     * {@code POST  /account-owners} : Create a new accountOwner.
     *
     * @param accountOwnerDTO the accountOwnerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accountOwnerDTO, or with status {@code 400 (Bad Request)} if the accountOwner has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/account-owners")
    public ResponseEntity<AccountOwnerDTO> createAccountOwner(@Valid @RequestBody AccountOwnerDTO accountOwnerDTO)
        throws URISyntaxException {
        log.debug("REST request to save AccountOwner : {}", accountOwnerDTO);
        if (accountOwnerDTO.getId() != null) {
            throw new BadRequestAlertException("A new accountOwner cannot already have an ID", ENTITY_NAME, "idexists");
        }

        if (accountOwnerRepository.existsByEmailOrUserReference(accountOwnerDTO.getEmail(), accountOwnerDTO.getUserReference())) {
            throw new BadRequestAlertException("An accountOwner already exists with email or userReference", ENTITY_NAME, "emailexists");
        }

        if (accountOwnerDTO.getFintechAccounts().size() > 1) {
            throw new BadRequestAlertException("A new accountOwner cannot have more than one account", ENTITY_NAME, "morethanoneaccount");
        }
        AccountOwnerDTO result = accountOwnerService.save(accountOwnerDTO);
        return ResponseEntity
            .created(new URI("/api/account-owners/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /account-owners/:id} : Updates an existing accountOwner.
     *
     * @param id the id of the accountOwnerDTO to save.
     * @param accountOwnerDTO the accountOwnerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountOwnerDTO,
     * or with status {@code 400 (Bad Request)} if the accountOwnerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accountOwnerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/account-owners/{id}")
    public ResponseEntity<AccountOwnerDTO> updateAccountOwner(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AccountOwnerDTO accountOwnerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AccountOwner : {}, {}", id, accountOwnerDTO);
        if (accountOwnerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountOwnerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountOwnerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AccountOwnerDTO result = accountOwnerService.save(accountOwnerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountOwnerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /account-owners/:id} : Partial updates given fields of an existing accountOwner, field will ignore if it is null
     *
     * @param id the id of the accountOwnerDTO to save.
     * @param accountOwnerDTO the accountOwnerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountOwnerDTO,
     * or with status {@code 400 (Bad Request)} if the accountOwnerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the accountOwnerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the accountOwnerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/account-owners/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AccountOwnerDTO> partialUpdateAccountOwner(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AccountOwnerDTO accountOwnerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AccountOwner partially : {}, {}", id, accountOwnerDTO);
        if (accountOwnerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountOwnerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountOwnerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AccountOwnerDTO> result = accountOwnerService.partialUpdate(accountOwnerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountOwnerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /account-owners} : get all the accountOwners.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accountOwners in body.
     */
    @GetMapping("/account-owners")
    public ResponseEntity<List<AccountOwnerDTO>> getAllAccountOwners(Pageable pageable) {
        log.debug("REST request to get a page of AccountOwners");
        Page<AccountOwnerDTO> page = accountOwnerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /account-owners/:id} : get the "id" accountOwner.
     *
     * @param id the id of the accountOwnerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accountOwnerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/account-owners/{id}")
    public ResponseEntity<AccountOwnerDTO> getAccountOwner(@PathVariable Long id) {
        log.debug("REST request to get AccountOwner : {}", id);
        Optional<AccountOwnerDTO> accountOwnerDTO = accountOwnerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accountOwnerDTO);
    }

    /**
     * {@code DELETE  /account-owners/:id} : delete the "id" accountOwner.
     *
     * @param id the id of the accountOwnerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/account-owners/{id}")
    public ResponseEntity<Void> deleteAccountOwner(@PathVariable Long id) {
        log.debug("REST request to delete AccountOwner : {}", id);
        accountOwnerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
