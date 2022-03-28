package com.victor.fintechaccountservice.service.dto;

import com.victor.fintechaccountservice.domain.enumeration.AccountType;
import com.victor.fintechaccountservice.domain.enumeration.RegistrationStatus;
import com.victor.fintechaccountservice.domain.enumeration.Status;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.victor.fintechaccountservice.domain.FintechAccount} entity.
 */
public class FintechAccountDTO implements Serializable {

    private Long id;

    @NotNull
    private AccountType accountType;

    @NotNull
    private String accountId;

    private Status accountStatus;

    private RegistrationStatus registrationStatus;

    private Double availableBalance;

    private Double ledgerBalance;

    private AccountOwnerDTO accountOwner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Status getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(Status accountStatus) {
        this.accountStatus = accountStatus;
    }

    public RegistrationStatus getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(RegistrationStatus registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public Double getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(Double availableBalance) {
        this.availableBalance = availableBalance;
    }

    public Double getLedgerBalance() {
        return ledgerBalance;
    }

    public void setLedgerBalance(Double ledgerBalance) {
        this.ledgerBalance = ledgerBalance;
    }

    public AccountOwnerDTO getAccountOwner() {
        return accountOwner;
    }

    public void setAccountOwner(AccountOwnerDTO accountOwner) {
        this.accountOwner = accountOwner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FintechAccountDTO)) {
            return false;
        }

        FintechAccountDTO fintechAccountDTO = (FintechAccountDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fintechAccountDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FintechAccountDTO{" +
            "id=" + getId() +
            ", accountType='" + getAccountType() + "'" +
            ", accountId='" + getAccountId() + "'" +
            ", accountStatus='" + getAccountStatus() + "'" +
            ", registrationStatus='" + getRegistrationStatus() + "'" +
            ", availableBalance=" + getAvailableBalance() +
            ", ledgerBalance=" + getLedgerBalance() +
            ", accountOwner=" + getAccountOwner() +
            "}";
    }
}
