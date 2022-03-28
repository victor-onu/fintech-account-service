package com.victor.fintechaccountservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.victor.fintechaccountservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccountOwnerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountOwner.class);
        AccountOwner accountOwner1 = new AccountOwner();
        accountOwner1.setId(1L);
        AccountOwner accountOwner2 = new AccountOwner();
        accountOwner2.setId(accountOwner1.getId());
        assertThat(accountOwner1).isEqualTo(accountOwner2);
        accountOwner2.setId(2L);
        assertThat(accountOwner1).isNotEqualTo(accountOwner2);
        accountOwner1.setId(null);
        assertThat(accountOwner1).isNotEqualTo(accountOwner2);
    }
}
