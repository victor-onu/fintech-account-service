package com.victor.fintechaccountservice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FintechAccountMapperTest {

    private FintechAccountMapper fintechAccountMapper;

    @BeforeEach
    public void setUp() {
        fintechAccountMapper = new FintechAccountMapperImpl();
    }
}
