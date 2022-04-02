package com.victor.fintechaccountservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.victor.fintechaccountservice.IntegrationTest;
import com.victor.fintechaccountservice.domain.AccountOwner;
import com.victor.fintechaccountservice.domain.enumeration.Gender;
import com.victor.fintechaccountservice.domain.enumeration.Status;
import com.victor.fintechaccountservice.repository.AccountOwnerRepository;
import com.victor.fintechaccountservice.service.dto.AccountOwnerDTO;
import com.victor.fintechaccountservice.service.fiegn.NotificationService;
import com.victor.fintechaccountservice.service.mapper.AccountOwnerMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AccountOwnerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AccountOwnerResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MIDDLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MIDDLE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "victor@gmail.com";
    private static final String UPDATED_EMAIL = "vee@gmail.com";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_USER_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_USER_REFERENCE = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final Status DEFAULT_STATUS = Status.ACTIVE;
    private static final Status UPDATED_STATUS = Status.INACTIVE;

    private static final String ENTITY_API_URL = "/api/account-owners";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AccountOwnerRepository accountOwnerRepository;

    @Autowired
    private AccountOwnerMapper accountOwnerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccountOwnerMockMvc;

    @MockBean
    private NotificationService notificationService;

    private AccountOwner accountOwner;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountOwner createEntity(EntityManager em) {
        AccountOwner accountOwner = new AccountOwner()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .middleName(DEFAULT_MIDDLE_NAME)
            .email(DEFAULT_EMAIL)
            .password(DEFAULT_PASSWORD)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .userReference(DEFAULT_USER_REFERENCE)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .address(DEFAULT_ADDRESS)
            .gender(DEFAULT_GENDER)
            .status(DEFAULT_STATUS);
        return accountOwner;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountOwner createUpdatedEntity(EntityManager em) {
        AccountOwner accountOwner = new AccountOwner()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .userReference(UPDATED_USER_REFERENCE)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .address(UPDATED_ADDRESS)
            .gender(UPDATED_GENDER)
            .status(UPDATED_STATUS);
        return accountOwner;
    }

    @BeforeEach
    public void initTest() {
        accountOwner = createEntity(em);
    }

    @Test
    @Transactional
    void createAccountOwner() throws Exception {
        int databaseSizeBeforeCreate = accountOwnerRepository.findAll().size();
        // Create the AccountOwner
        AccountOwnerDTO accountOwnerDTO = accountOwnerMapper.toDto(accountOwner);

        Mockito.when(notificationService.sendCreateAccountMail(any(AccountOwnerDTO.class))).thenReturn(true);
        restAccountOwnerMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountOwnerDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AccountOwner in the database
        List<AccountOwner> accountOwnerList = accountOwnerRepository.findAll();
        assertThat(accountOwnerList).hasSize(databaseSizeBeforeCreate + 1);
        AccountOwner testAccountOwner = accountOwnerList.get(accountOwnerList.size() - 1);
        assertThat(testAccountOwner.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testAccountOwner.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testAccountOwner.getMiddleName()).isEqualTo(DEFAULT_MIDDLE_NAME);
        assertThat(testAccountOwner.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testAccountOwner.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testAccountOwner.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testAccountOwner.getUserReference()).isEqualTo(DEFAULT_USER_REFERENCE);
        assertThat(testAccountOwner.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testAccountOwner.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testAccountOwner.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testAccountOwner.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createAccountOwnerWithExistingId() throws Exception {
        // Create the AccountOwner with an existing ID
        accountOwner.setId(1L);
        AccountOwnerDTO accountOwnerDTO = accountOwnerMapper.toDto(accountOwner);

        int databaseSizeBeforeCreate = accountOwnerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountOwnerMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountOwnerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountOwner in the database
        List<AccountOwner> accountOwnerList = accountOwnerRepository.findAll();
        assertThat(accountOwnerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountOwnerRepository.findAll().size();
        // set the field null
        accountOwner.setFirstName(null);

        // Create the AccountOwner, which fails.
        AccountOwnerDTO accountOwnerDTO = accountOwnerMapper.toDto(accountOwner);

        restAccountOwnerMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountOwnerDTO))
            )
            .andExpect(status().isBadRequest());

        List<AccountOwner> accountOwnerList = accountOwnerRepository.findAll();
        assertThat(accountOwnerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountOwnerRepository.findAll().size();
        // set the field null
        accountOwner.setLastName(null);

        // Create the AccountOwner, which fails.
        AccountOwnerDTO accountOwnerDTO = accountOwnerMapper.toDto(accountOwner);

        restAccountOwnerMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountOwnerDTO))
            )
            .andExpect(status().isBadRequest());

        List<AccountOwner> accountOwnerList = accountOwnerRepository.findAll();
        assertThat(accountOwnerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountOwnerRepository.findAll().size();
        // set the field null
        accountOwner.setEmail(null);

        // Create the AccountOwner, which fails.
        AccountOwnerDTO accountOwnerDTO = accountOwnerMapper.toDto(accountOwner);

        restAccountOwnerMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountOwnerDTO))
            )
            .andExpect(status().isBadRequest());

        List<AccountOwner> accountOwnerList = accountOwnerRepository.findAll();
        assertThat(accountOwnerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserReferenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountOwnerRepository.findAll().size();
        // set the field null
        accountOwner.setUserReference(null);

        // Create the AccountOwner, which fails.
        AccountOwnerDTO accountOwnerDTO = accountOwnerMapper.toDto(accountOwner);

        restAccountOwnerMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountOwnerDTO))
            )
            .andExpect(status().isBadRequest());

        List<AccountOwner> accountOwnerList = accountOwnerRepository.findAll();
        assertThat(accountOwnerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAccountOwners() throws Exception {
        // Initialize the database
        accountOwnerRepository.saveAndFlush(accountOwner);

        // Get all the accountOwnerList
        restAccountOwnerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountOwner.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].userReference").value(hasItem(DEFAULT_USER_REFERENCE)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getAccountOwner() throws Exception {
        // Initialize the database
        accountOwnerRepository.saveAndFlush(accountOwner);

        // Get the accountOwner
        restAccountOwnerMockMvc
            .perform(get(ENTITY_API_URL_ID, accountOwner.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accountOwner.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.middleName").value(DEFAULT_MIDDLE_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.userReference").value(DEFAULT_USER_REFERENCE))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAccountOwner() throws Exception {
        // Get the accountOwner
        restAccountOwnerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAccountOwner() throws Exception {
        // Initialize the database
        accountOwnerRepository.saveAndFlush(accountOwner);

        int databaseSizeBeforeUpdate = accountOwnerRepository.findAll().size();

        // Update the accountOwner
        AccountOwner updatedAccountOwner = accountOwnerRepository.findById(accountOwner.getId()).get();
        // Disconnect from session so that the updates on updatedAccountOwner are not directly saved in db
        em.detach(updatedAccountOwner);
        updatedAccountOwner
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .userReference(UPDATED_USER_REFERENCE)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .address(UPDATED_ADDRESS)
            .gender(UPDATED_GENDER)
            .status(UPDATED_STATUS);
        AccountOwnerDTO accountOwnerDTO = accountOwnerMapper.toDto(updatedAccountOwner);

        restAccountOwnerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accountOwnerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountOwnerDTO))
            )
            .andExpect(status().isOk());

        // Validate the AccountOwner in the database
        List<AccountOwner> accountOwnerList = accountOwnerRepository.findAll();
        assertThat(accountOwnerList).hasSize(databaseSizeBeforeUpdate);
        AccountOwner testAccountOwner = accountOwnerList.get(accountOwnerList.size() - 1);
        assertThat(testAccountOwner.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testAccountOwner.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testAccountOwner.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testAccountOwner.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAccountOwner.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testAccountOwner.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testAccountOwner.getUserReference()).isEqualTo(UPDATED_USER_REFERENCE);
        assertThat(testAccountOwner.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testAccountOwner.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testAccountOwner.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testAccountOwner.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingAccountOwner() throws Exception {
        int databaseSizeBeforeUpdate = accountOwnerRepository.findAll().size();
        accountOwner.setId(count.incrementAndGet());

        // Create the AccountOwner
        AccountOwnerDTO accountOwnerDTO = accountOwnerMapper.toDto(accountOwner);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountOwnerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accountOwnerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountOwnerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountOwner in the database
        List<AccountOwner> accountOwnerList = accountOwnerRepository.findAll();
        assertThat(accountOwnerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAccountOwner() throws Exception {
        int databaseSizeBeforeUpdate = accountOwnerRepository.findAll().size();
        accountOwner.setId(count.incrementAndGet());

        // Create the AccountOwner
        AccountOwnerDTO accountOwnerDTO = accountOwnerMapper.toDto(accountOwner);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountOwnerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountOwnerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountOwner in the database
        List<AccountOwner> accountOwnerList = accountOwnerRepository.findAll();
        assertThat(accountOwnerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAccountOwner() throws Exception {
        int databaseSizeBeforeUpdate = accountOwnerRepository.findAll().size();
        accountOwner.setId(count.incrementAndGet());

        // Create the AccountOwner
        AccountOwnerDTO accountOwnerDTO = accountOwnerMapper.toDto(accountOwner);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountOwnerMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountOwnerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountOwner in the database
        List<AccountOwner> accountOwnerList = accountOwnerRepository.findAll();
        assertThat(accountOwnerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAccountOwnerWithPatch() throws Exception {
        // Initialize the database
        accountOwnerRepository.saveAndFlush(accountOwner);

        int databaseSizeBeforeUpdate = accountOwnerRepository.findAll().size();

        // Update the accountOwner using partial update
        AccountOwner partialUpdatedAccountOwner = new AccountOwner();
        partialUpdatedAccountOwner.setId(accountOwner.getId());

        partialUpdatedAccountOwner
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .userReference(UPDATED_USER_REFERENCE)
            .address(UPDATED_ADDRESS)
            .gender(UPDATED_GENDER);

        restAccountOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountOwner.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccountOwner))
            )
            .andExpect(status().isOk());

        // Validate the AccountOwner in the database
        List<AccountOwner> accountOwnerList = accountOwnerRepository.findAll();
        assertThat(accountOwnerList).hasSize(databaseSizeBeforeUpdate);
        AccountOwner testAccountOwner = accountOwnerList.get(accountOwnerList.size() - 1);
        assertThat(testAccountOwner.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testAccountOwner.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testAccountOwner.getMiddleName()).isEqualTo(DEFAULT_MIDDLE_NAME);
        assertThat(testAccountOwner.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAccountOwner.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testAccountOwner.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testAccountOwner.getUserReference()).isEqualTo(UPDATED_USER_REFERENCE);
        assertThat(testAccountOwner.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testAccountOwner.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testAccountOwner.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testAccountOwner.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateAccountOwnerWithPatch() throws Exception {
        // Initialize the database
        accountOwnerRepository.saveAndFlush(accountOwner);

        int databaseSizeBeforeUpdate = accountOwnerRepository.findAll().size();

        // Update the accountOwner using partial update
        AccountOwner partialUpdatedAccountOwner = new AccountOwner();
        partialUpdatedAccountOwner.setId(accountOwner.getId());

        partialUpdatedAccountOwner
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .userReference(UPDATED_USER_REFERENCE)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .address(UPDATED_ADDRESS)
            .gender(UPDATED_GENDER)
            .status(UPDATED_STATUS);

        restAccountOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountOwner.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccountOwner))
            )
            .andExpect(status().isOk());

        // Validate the AccountOwner in the database
        List<AccountOwner> accountOwnerList = accountOwnerRepository.findAll();
        assertThat(accountOwnerList).hasSize(databaseSizeBeforeUpdate);
        AccountOwner testAccountOwner = accountOwnerList.get(accountOwnerList.size() - 1);
        assertThat(testAccountOwner.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testAccountOwner.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testAccountOwner.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testAccountOwner.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAccountOwner.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testAccountOwner.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testAccountOwner.getUserReference()).isEqualTo(UPDATED_USER_REFERENCE);
        assertThat(testAccountOwner.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testAccountOwner.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testAccountOwner.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testAccountOwner.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingAccountOwner() throws Exception {
        int databaseSizeBeforeUpdate = accountOwnerRepository.findAll().size();
        accountOwner.setId(count.incrementAndGet());

        // Create the AccountOwner
        AccountOwnerDTO accountOwnerDTO = accountOwnerMapper.toDto(accountOwner);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accountOwnerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountOwnerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountOwner in the database
        List<AccountOwner> accountOwnerList = accountOwnerRepository.findAll();
        assertThat(accountOwnerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAccountOwner() throws Exception {
        int databaseSizeBeforeUpdate = accountOwnerRepository.findAll().size();
        accountOwner.setId(count.incrementAndGet());

        // Create the AccountOwner
        AccountOwnerDTO accountOwnerDTO = accountOwnerMapper.toDto(accountOwner);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountOwnerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountOwner in the database
        List<AccountOwner> accountOwnerList = accountOwnerRepository.findAll();
        assertThat(accountOwnerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAccountOwner() throws Exception {
        int databaseSizeBeforeUpdate = accountOwnerRepository.findAll().size();
        accountOwner.setId(count.incrementAndGet());

        // Create the AccountOwner
        AccountOwnerDTO accountOwnerDTO = accountOwnerMapper.toDto(accountOwner);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountOwnerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountOwner in the database
        List<AccountOwner> accountOwnerList = accountOwnerRepository.findAll();
        assertThat(accountOwnerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAccountOwner() throws Exception {
        // Initialize the database
        accountOwnerRepository.saveAndFlush(accountOwner);

        int databaseSizeBeforeDelete = accountOwnerRepository.findAll().size();

        // Delete the accountOwner
        restAccountOwnerMockMvc
            .perform(delete(ENTITY_API_URL_ID, accountOwner.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AccountOwner> accountOwnerList = accountOwnerRepository.findAll();
        assertThat(accountOwnerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
