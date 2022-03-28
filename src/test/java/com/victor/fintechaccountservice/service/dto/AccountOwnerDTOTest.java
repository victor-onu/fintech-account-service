package com.victor.fintechaccountservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.victor.fintechaccountservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccountOwnerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountOwnerDTO.class);
        AccountOwnerDTO accountOwnerDTO1 = new AccountOwnerDTO();
        accountOwnerDTO1.setId(1L);
        AccountOwnerDTO accountOwnerDTO2 = new AccountOwnerDTO();
        assertThat(accountOwnerDTO1).isNotEqualTo(accountOwnerDTO2);
        accountOwnerDTO2.setId(accountOwnerDTO1.getId());
        assertThat(accountOwnerDTO1).isEqualTo(accountOwnerDTO2);
        accountOwnerDTO2.setId(2L);
        assertThat(accountOwnerDTO1).isNotEqualTo(accountOwnerDTO2);
        accountOwnerDTO1.setId(null);
        assertThat(accountOwnerDTO1).isNotEqualTo(accountOwnerDTO2);
    }
}
