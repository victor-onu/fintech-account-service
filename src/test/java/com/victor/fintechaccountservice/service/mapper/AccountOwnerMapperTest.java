package com.victor.fintechaccountservice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AccountOwnerMapperTest {

    private AccountOwnerMapper accountOwnerMapper;

    @BeforeEach
    public void setUp() {
        accountOwnerMapper = new AccountOwnerMapperImpl();
    }
}
