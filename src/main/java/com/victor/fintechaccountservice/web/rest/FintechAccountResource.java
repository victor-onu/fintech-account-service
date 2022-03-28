package com.victor.fintechaccountservice.web.rest;

import com.victor.fintechaccountservice.repository.FintechAccountRepository;
import com.victor.fintechaccountservice.service.FintechAccountService;
import com.victor.fintechaccountservice.service.dto.FintechAccountDTO;
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
 * REST controller for managing {@link com.victor.fintechaccountservice.domain.FintechAccount}.
 */
@RestController
@RequestMapping("/api")
public class FintechAccountResource {

    private final Logger log = LoggerFactory.getLogger(FintechAccountResource.class);

    private static final String ENTITY_NAME = "fintechAccountServiceFintechAccount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FintechAccountService fintechAccountService;

    private final FintechAccountRepository fintechAccountRepository;

    public FintechAccountResource(FintechAccountService fintechAccountService, FintechAccountRepository fintechAccountRepository) {
        this.fintechAccountService = fintechAccountService;
        this.fintechAccountRepository = fintechAccountRepository;
    }

    /**
     * {@code POST  /fintech-accounts} : Create a new fintechAccount.
     *
     * @param fintechAccountDTO the fintechAccountDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fintechAccountDTO, or with status {@code 400 (Bad Request)} if the fintechAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fintech-accounts")
    public ResponseEntity<FintechAccountDTO> createFintechAccount(@Valid @RequestBody FintechAccountDTO fintechAccountDTO)
        throws URISyntaxException {
        log.debug("REST request to save FintechAccount : {}", fintechAccountDTO);
        if (fintechAccountDTO.getId() != null) {
            throw new BadRequestAlertException("A new fintechAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FintechAccountDTO result = fintechAccountService.save(fintechAccountDTO);
        return ResponseEntity
            .created(new URI("/api/fintech-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fintech-accounts/:id} : Updates an existing fintechAccount.
     *
     * @param id the id of the fintechAccountDTO to save.
     * @param fintechAccountDTO the fintechAccountDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fintechAccountDTO,
     * or with status {@code 400 (Bad Request)} if the fintechAccountDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fintechAccountDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fintech-accounts/{id}")
    public ResponseEntity<FintechAccountDTO> updateFintechAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FintechAccountDTO fintechAccountDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FintechAccount : {}, {}", id, fintechAccountDTO);
        if (fintechAccountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fintechAccountDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fintechAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FintechAccountDTO result = fintechAccountService.save(fintechAccountDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fintechAccountDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fintech-accounts/:id} : Partial updates given fields of an existing fintechAccount, field will ignore if it is null
     *
     * @param id the id of the fintechAccountDTO to save.
     * @param fintechAccountDTO the fintechAccountDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fintechAccountDTO,
     * or with status {@code 400 (Bad Request)} if the fintechAccountDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fintechAccountDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fintechAccountDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fintech-accounts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FintechAccountDTO> partialUpdateFintechAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FintechAccountDTO fintechAccountDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FintechAccount partially : {}, {}", id, fintechAccountDTO);
        if (fintechAccountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fintechAccountDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fintechAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FintechAccountDTO> result = fintechAccountService.partialUpdate(fintechAccountDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fintechAccountDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /fintech-accounts} : get all the fintechAccounts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fintechAccounts in body.
     */
    @GetMapping("/fintech-accounts")
    public ResponseEntity<List<FintechAccountDTO>> getAllFintechAccounts(Pageable pageable) {
        log.debug("REST request to get a page of FintechAccounts");
        Page<FintechAccountDTO> page = fintechAccountService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fintech-accounts/:id} : get the "id" fintechAccount.
     *
     * @param id the id of the fintechAccountDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fintechAccountDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fintech-accounts/{id}")
    public ResponseEntity<FintechAccountDTO> getFintechAccount(@PathVariable Long id) {
        log.debug("REST request to get FintechAccount : {}", id);
        Optional<FintechAccountDTO> fintechAccountDTO = fintechAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fintechAccountDTO);
    }

    /**
     * {@code DELETE  /fintech-accounts/:id} : delete the "id" fintechAccount.
     *
     * @param id the id of the fintechAccountDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fintech-accounts/{id}")
    public ResponseEntity<Void> deleteFintechAccount(@PathVariable Long id) {
        log.debug("REST request to delete FintechAccount : {}", id);
        fintechAccountService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
