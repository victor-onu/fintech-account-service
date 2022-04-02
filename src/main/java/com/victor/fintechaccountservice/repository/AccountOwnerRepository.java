package com.victor.fintechaccountservice.repository;

import com.victor.fintechaccountservice.domain.AccountOwner;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AccountOwner entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountOwnerRepository extends JpaRepository<AccountOwner, Long> {
    boolean existsByEmailOrUserReference(String email, String userRref);
}
