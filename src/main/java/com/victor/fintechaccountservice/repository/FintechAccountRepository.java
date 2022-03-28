package com.victor.fintechaccountservice.repository;

import com.victor.fintechaccountservice.domain.FintechAccount;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FintechAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FintechAccountRepository extends JpaRepository<FintechAccount, Long> {}
