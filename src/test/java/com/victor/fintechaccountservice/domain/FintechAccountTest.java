package com.victor.fintechaccountservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.victor.fintechaccountservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FintechAccountTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FintechAccount.class);
        FintechAccount fintechAccount1 = new FintechAccount();
        fintechAccount1.setId(1L);
        FintechAccount fintechAccount2 = new FintechAccount();
        fintechAccount2.setId(fintechAccount1.getId());
        assertThat(fintechAccount1).isEqualTo(fintechAccount2);
        fintechAccount2.setId(2L);
        assertThat(fintechAccount1).isNotEqualTo(fintechAccount2);
        fintechAccount1.setId(null);
        assertThat(fintechAccount1).isNotEqualTo(fintechAccount2);
    }
}
