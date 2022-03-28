package com.victor.fintechaccountservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.victor.fintechaccountservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FintechAccountDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FintechAccountDTO.class);
        FintechAccountDTO fintechAccountDTO1 = new FintechAccountDTO();
        fintechAccountDTO1.setId(1L);
        FintechAccountDTO fintechAccountDTO2 = new FintechAccountDTO();
        assertThat(fintechAccountDTO1).isNotEqualTo(fintechAccountDTO2);
        fintechAccountDTO2.setId(fintechAccountDTO1.getId());
        assertThat(fintechAccountDTO1).isEqualTo(fintechAccountDTO2);
        fintechAccountDTO2.setId(2L);
        assertThat(fintechAccountDTO1).isNotEqualTo(fintechAccountDTO2);
        fintechAccountDTO1.setId(null);
        assertThat(fintechAccountDTO1).isNotEqualTo(fintechAccountDTO2);
    }
}
