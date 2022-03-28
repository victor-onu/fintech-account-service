package com.victor.fintechaccountservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.victor.fintechaccountservice.IntegrationTest;
import com.victor.fintechaccountservice.domain.FintechAccount;
import com.victor.fintechaccountservice.domain.enumeration.AccountType;
import com.victor.fintechaccountservice.domain.enumeration.RegistrationStatus;
import com.victor.fintechaccountservice.domain.enumeration.Status;
import com.victor.fintechaccountservice.repository.FintechAccountRepository;
import com.victor.fintechaccountservice.service.dto.FintechAccountDTO;
import com.victor.fintechaccountservice.service.mapper.FintechAccountMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FintechAccountResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FintechAccountResourceIT {

    private static final AccountType DEFAULT_ACCOUNT_TYPE = AccountType.WALLET;
    private static final AccountType UPDATED_ACCOUNT_TYPE = AccountType.BANK;

    private static final String DEFAULT_ACCOUNT_ID = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_ID = "BBBBBBBBBB";

    private static final Status DEFAULT_ACCOUNT_STATUS = Status.ACTIVE;
    private static final Status UPDATED_ACCOUNT_STATUS = Status.INACTIVE;

    private static final RegistrationStatus DEFAULT_REGISTRATION_STATUS = RegistrationStatus.PENDING;
    private static final RegistrationStatus UPDATED_REGISTRATION_STATUS = RegistrationStatus.VERIFIED;

    private static final Double DEFAULT_AVAILABLE_BALANCE = 1D;
    private static final Double UPDATED_AVAILABLE_BALANCE = 2D;

    private static final Double DEFAULT_LEDGER_BALANCE = 1D;
    private static final Double UPDATED_LEDGER_BALANCE = 2D;

    private static final String ENTITY_API_URL = "/api/fintech-accounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FintechAccountRepository fintechAccountRepository;

    @Autowired
    private FintechAccountMapper fintechAccountMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFintechAccountMockMvc;

    private FintechAccount fintechAccount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FintechAccount createEntity(EntityManager em) {
        FintechAccount fintechAccount = new FintechAccount()
            .accountType(DEFAULT_ACCOUNT_TYPE)
            .accountId(DEFAULT_ACCOUNT_ID)
            .accountStatus(DEFAULT_ACCOUNT_STATUS)
            .registrationStatus(DEFAULT_REGISTRATION_STATUS)
            .availableBalance(DEFAULT_AVAILABLE_BALANCE)
            .ledgerBalance(DEFAULT_LEDGER_BALANCE);
        return fintechAccount;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FintechAccount createUpdatedEntity(EntityManager em) {
        FintechAccount fintechAccount = new FintechAccount()
            .accountType(UPDATED_ACCOUNT_TYPE)
            .accountId(UPDATED_ACCOUNT_ID)
            .accountStatus(UPDATED_ACCOUNT_STATUS)
            .registrationStatus(UPDATED_REGISTRATION_STATUS)
            .availableBalance(UPDATED_AVAILABLE_BALANCE)
            .ledgerBalance(UPDATED_LEDGER_BALANCE);
        return fintechAccount;
    }

    @BeforeEach
    public void initTest() {
        fintechAccount = createEntity(em);
    }

    @Test
    @Transactional
    void createFintechAccount() throws Exception {
        int databaseSizeBeforeCreate = fintechAccountRepository.findAll().size();
        // Create the FintechAccount
        FintechAccountDTO fintechAccountDTO = fintechAccountMapper.toDto(fintechAccount);
        restFintechAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fintechAccountDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FintechAccount in the database
        List<FintechAccount> fintechAccountList = fintechAccountRepository.findAll();
        assertThat(fintechAccountList).hasSize(databaseSizeBeforeCreate + 1);
        FintechAccount testFintechAccount = fintechAccountList.get(fintechAccountList.size() - 1);
        assertThat(testFintechAccount.getAccountType()).isEqualTo(DEFAULT_ACCOUNT_TYPE);
        assertThat(testFintechAccount.getAccountId()).isEqualTo(DEFAULT_ACCOUNT_ID);
        assertThat(testFintechAccount.getAccountStatus()).isEqualTo(DEFAULT_ACCOUNT_STATUS);
        assertThat(testFintechAccount.getRegistrationStatus()).isEqualTo(DEFAULT_REGISTRATION_STATUS);
        assertThat(testFintechAccount.getAvailableBalance()).isEqualTo(DEFAULT_AVAILABLE_BALANCE);
        assertThat(testFintechAccount.getLedgerBalance()).isEqualTo(DEFAULT_LEDGER_BALANCE);
    }

    @Test
    @Transactional
    void createFintechAccountWithExistingId() throws Exception {
        // Create the FintechAccount with an existing ID
        fintechAccount.setId(1L);
        FintechAccountDTO fintechAccountDTO = fintechAccountMapper.toDto(fintechAccount);

        int databaseSizeBeforeCreate = fintechAccountRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFintechAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fintechAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FintechAccount in the database
        List<FintechAccount> fintechAccountList = fintechAccountRepository.findAll();
        assertThat(fintechAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAccountTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fintechAccountRepository.findAll().size();
        // set the field null
        fintechAccount.setAccountType(null);

        // Create the FintechAccount, which fails.
        FintechAccountDTO fintechAccountDTO = fintechAccountMapper.toDto(fintechAccount);

        restFintechAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fintechAccountDTO))
            )
            .andExpect(status().isBadRequest());

        List<FintechAccount> fintechAccountList = fintechAccountRepository.findAll();
        assertThat(fintechAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAccountIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = fintechAccountRepository.findAll().size();
        // set the field null
        fintechAccount.setAccountId(null);

        // Create the FintechAccount, which fails.
        FintechAccountDTO fintechAccountDTO = fintechAccountMapper.toDto(fintechAccount);

        restFintechAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fintechAccountDTO))
            )
            .andExpect(status().isBadRequest());

        List<FintechAccount> fintechAccountList = fintechAccountRepository.findAll();
        assertThat(fintechAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFintechAccounts() throws Exception {
        // Initialize the database
        fintechAccountRepository.saveAndFlush(fintechAccount);

        // Get all the fintechAccountList
        restFintechAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fintechAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountType").value(hasItem(DEFAULT_ACCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].accountId").value(hasItem(DEFAULT_ACCOUNT_ID)))
            .andExpect(jsonPath("$.[*].accountStatus").value(hasItem(DEFAULT_ACCOUNT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].registrationStatus").value(hasItem(DEFAULT_REGISTRATION_STATUS.toString())))
            .andExpect(jsonPath("$.[*].availableBalance").value(hasItem(DEFAULT_AVAILABLE_BALANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].ledgerBalance").value(hasItem(DEFAULT_LEDGER_BALANCE.doubleValue())));
    }

    @Test
    @Transactional
    void getFintechAccount() throws Exception {
        // Initialize the database
        fintechAccountRepository.saveAndFlush(fintechAccount);

        // Get the fintechAccount
        restFintechAccountMockMvc
            .perform(get(ENTITY_API_URL_ID, fintechAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fintechAccount.getId().intValue()))
            .andExpect(jsonPath("$.accountType").value(DEFAULT_ACCOUNT_TYPE.toString()))
            .andExpect(jsonPath("$.accountId").value(DEFAULT_ACCOUNT_ID))
            .andExpect(jsonPath("$.accountStatus").value(DEFAULT_ACCOUNT_STATUS.toString()))
            .andExpect(jsonPath("$.registrationStatus").value(DEFAULT_REGISTRATION_STATUS.toString()))
            .andExpect(jsonPath("$.availableBalance").value(DEFAULT_AVAILABLE_BALANCE.doubleValue()))
            .andExpect(jsonPath("$.ledgerBalance").value(DEFAULT_LEDGER_BALANCE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingFintechAccount() throws Exception {
        // Get the fintechAccount
        restFintechAccountMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFintechAccount() throws Exception {
        // Initialize the database
        fintechAccountRepository.saveAndFlush(fintechAccount);

        int databaseSizeBeforeUpdate = fintechAccountRepository.findAll().size();

        // Update the fintechAccount
        FintechAccount updatedFintechAccount = fintechAccountRepository.findById(fintechAccount.getId()).get();
        // Disconnect from session so that the updates on updatedFintechAccount are not directly saved in db
        em.detach(updatedFintechAccount);
        updatedFintechAccount
            .accountType(UPDATED_ACCOUNT_TYPE)
            .accountId(UPDATED_ACCOUNT_ID)
            .accountStatus(UPDATED_ACCOUNT_STATUS)
            .registrationStatus(UPDATED_REGISTRATION_STATUS)
            .availableBalance(UPDATED_AVAILABLE_BALANCE)
            .ledgerBalance(UPDATED_LEDGER_BALANCE);
        FintechAccountDTO fintechAccountDTO = fintechAccountMapper.toDto(updatedFintechAccount);

        restFintechAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fintechAccountDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fintechAccountDTO))
            )
            .andExpect(status().isOk());

        // Validate the FintechAccount in the database
        List<FintechAccount> fintechAccountList = fintechAccountRepository.findAll();
        assertThat(fintechAccountList).hasSize(databaseSizeBeforeUpdate);
        FintechAccount testFintechAccount = fintechAccountList.get(fintechAccountList.size() - 1);
        assertThat(testFintechAccount.getAccountType()).isEqualTo(UPDATED_ACCOUNT_TYPE);
        assertThat(testFintechAccount.getAccountId()).isEqualTo(UPDATED_ACCOUNT_ID);
        assertThat(testFintechAccount.getAccountStatus()).isEqualTo(UPDATED_ACCOUNT_STATUS);
        assertThat(testFintechAccount.getRegistrationStatus()).isEqualTo(UPDATED_REGISTRATION_STATUS);
        assertThat(testFintechAccount.getAvailableBalance()).isEqualTo(UPDATED_AVAILABLE_BALANCE);
        assertThat(testFintechAccount.getLedgerBalance()).isEqualTo(UPDATED_LEDGER_BALANCE);
    }

    @Test
    @Transactional
    void putNonExistingFintechAccount() throws Exception {
        int databaseSizeBeforeUpdate = fintechAccountRepository.findAll().size();
        fintechAccount.setId(count.incrementAndGet());

        // Create the FintechAccount
        FintechAccountDTO fintechAccountDTO = fintechAccountMapper.toDto(fintechAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFintechAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fintechAccountDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fintechAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FintechAccount in the database
        List<FintechAccount> fintechAccountList = fintechAccountRepository.findAll();
        assertThat(fintechAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFintechAccount() throws Exception {
        int databaseSizeBeforeUpdate = fintechAccountRepository.findAll().size();
        fintechAccount.setId(count.incrementAndGet());

        // Create the FintechAccount
        FintechAccountDTO fintechAccountDTO = fintechAccountMapper.toDto(fintechAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFintechAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fintechAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FintechAccount in the database
        List<FintechAccount> fintechAccountList = fintechAccountRepository.findAll();
        assertThat(fintechAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFintechAccount() throws Exception {
        int databaseSizeBeforeUpdate = fintechAccountRepository.findAll().size();
        fintechAccount.setId(count.incrementAndGet());

        // Create the FintechAccount
        FintechAccountDTO fintechAccountDTO = fintechAccountMapper.toDto(fintechAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFintechAccountMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fintechAccountDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FintechAccount in the database
        List<FintechAccount> fintechAccountList = fintechAccountRepository.findAll();
        assertThat(fintechAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFintechAccountWithPatch() throws Exception {
        // Initialize the database
        fintechAccountRepository.saveAndFlush(fintechAccount);

        int databaseSizeBeforeUpdate = fintechAccountRepository.findAll().size();

        // Update the fintechAccount using partial update
        FintechAccount partialUpdatedFintechAccount = new FintechAccount();
        partialUpdatedFintechAccount.setId(fintechAccount.getId());

        partialUpdatedFintechAccount
            .accountType(UPDATED_ACCOUNT_TYPE)
            .accountId(UPDATED_ACCOUNT_ID)
            .accountStatus(UPDATED_ACCOUNT_STATUS)
            .availableBalance(UPDATED_AVAILABLE_BALANCE)
            .ledgerBalance(UPDATED_LEDGER_BALANCE);

        restFintechAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFintechAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFintechAccount))
            )
            .andExpect(status().isOk());

        // Validate the FintechAccount in the database
        List<FintechAccount> fintechAccountList = fintechAccountRepository.findAll();
        assertThat(fintechAccountList).hasSize(databaseSizeBeforeUpdate);
        FintechAccount testFintechAccount = fintechAccountList.get(fintechAccountList.size() - 1);
        assertThat(testFintechAccount.getAccountType()).isEqualTo(UPDATED_ACCOUNT_TYPE);
        assertThat(testFintechAccount.getAccountId()).isEqualTo(UPDATED_ACCOUNT_ID);
        assertThat(testFintechAccount.getAccountStatus()).isEqualTo(UPDATED_ACCOUNT_STATUS);
        assertThat(testFintechAccount.getRegistrationStatus()).isEqualTo(DEFAULT_REGISTRATION_STATUS);
        assertThat(testFintechAccount.getAvailableBalance()).isEqualTo(UPDATED_AVAILABLE_BALANCE);
        assertThat(testFintechAccount.getLedgerBalance()).isEqualTo(UPDATED_LEDGER_BALANCE);
    }

    @Test
    @Transactional
    void fullUpdateFintechAccountWithPatch() throws Exception {
        // Initialize the database
        fintechAccountRepository.saveAndFlush(fintechAccount);

        int databaseSizeBeforeUpdate = fintechAccountRepository.findAll().size();

        // Update the fintechAccount using partial update
        FintechAccount partialUpdatedFintechAccount = new FintechAccount();
        partialUpdatedFintechAccount.setId(fintechAccount.getId());

        partialUpdatedFintechAccount
            .accountType(UPDATED_ACCOUNT_TYPE)
            .accountId(UPDATED_ACCOUNT_ID)
            .accountStatus(UPDATED_ACCOUNT_STATUS)
            .registrationStatus(UPDATED_REGISTRATION_STATUS)
            .availableBalance(UPDATED_AVAILABLE_BALANCE)
            .ledgerBalance(UPDATED_LEDGER_BALANCE);

        restFintechAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFintechAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFintechAccount))
            )
            .andExpect(status().isOk());

        // Validate the FintechAccount in the database
        List<FintechAccount> fintechAccountList = fintechAccountRepository.findAll();
        assertThat(fintechAccountList).hasSize(databaseSizeBeforeUpdate);
        FintechAccount testFintechAccount = fintechAccountList.get(fintechAccountList.size() - 1);
        assertThat(testFintechAccount.getAccountType()).isEqualTo(UPDATED_ACCOUNT_TYPE);
        assertThat(testFintechAccount.getAccountId()).isEqualTo(UPDATED_ACCOUNT_ID);
        assertThat(testFintechAccount.getAccountStatus()).isEqualTo(UPDATED_ACCOUNT_STATUS);
        assertThat(testFintechAccount.getRegistrationStatus()).isEqualTo(UPDATED_REGISTRATION_STATUS);
        assertThat(testFintechAccount.getAvailableBalance()).isEqualTo(UPDATED_AVAILABLE_BALANCE);
        assertThat(testFintechAccount.getLedgerBalance()).isEqualTo(UPDATED_LEDGER_BALANCE);
    }

    @Test
    @Transactional
    void patchNonExistingFintechAccount() throws Exception {
        int databaseSizeBeforeUpdate = fintechAccountRepository.findAll().size();
        fintechAccount.setId(count.incrementAndGet());

        // Create the FintechAccount
        FintechAccountDTO fintechAccountDTO = fintechAccountMapper.toDto(fintechAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFintechAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fintechAccountDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fintechAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FintechAccount in the database
        List<FintechAccount> fintechAccountList = fintechAccountRepository.findAll();
        assertThat(fintechAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFintechAccount() throws Exception {
        int databaseSizeBeforeUpdate = fintechAccountRepository.findAll().size();
        fintechAccount.setId(count.incrementAndGet());

        // Create the FintechAccount
        FintechAccountDTO fintechAccountDTO = fintechAccountMapper.toDto(fintechAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFintechAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fintechAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FintechAccount in the database
        List<FintechAccount> fintechAccountList = fintechAccountRepository.findAll();
        assertThat(fintechAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFintechAccount() throws Exception {
        int databaseSizeBeforeUpdate = fintechAccountRepository.findAll().size();
        fintechAccount.setId(count.incrementAndGet());

        // Create the FintechAccount
        FintechAccountDTO fintechAccountDTO = fintechAccountMapper.toDto(fintechAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFintechAccountMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fintechAccountDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FintechAccount in the database
        List<FintechAccount> fintechAccountList = fintechAccountRepository.findAll();
        assertThat(fintechAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFintechAccount() throws Exception {
        // Initialize the database
        fintechAccountRepository.saveAndFlush(fintechAccount);

        int databaseSizeBeforeDelete = fintechAccountRepository.findAll().size();

        // Delete the fintechAccount
        restFintechAccountMockMvc
            .perform(delete(ENTITY_API_URL_ID, fintechAccount.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FintechAccount> fintechAccountList = fintechAccountRepository.findAll();
        assertThat(fintechAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
