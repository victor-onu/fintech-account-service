package com.victor.fintechaccountservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.victor.fintechaccountservice.domain.enumeration.AccountType;
import com.victor.fintechaccountservice.domain.enumeration.RegistrationStatus;
import com.victor.fintechaccountservice.domain.enumeration.Status;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A FintechAccount.
 */
@Entity
@Table(name = "fintech_account")
public class FintechAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @NotNull
    @Column(name = "account_id", nullable = false)
    private String accountId;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status")
    private Status accountStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "registration_status")
    private RegistrationStatus registrationStatus;

    @Column(name = "available_balance")
    private Double availableBalance;

    @Column(name = "ledger_balance")
    private Double ledgerBalance;

    @ManyToOne
    @JsonIgnoreProperties(value = { "fintechAccounts" }, allowSetters = true)
    private AccountOwner accountOwner;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FintechAccount id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AccountType getAccountType() {
        return this.accountType;
    }

    public FintechAccount accountType(AccountType accountType) {
        this.setAccountType(accountType);
        return this;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public FintechAccount accountId(String accountId) {
        this.setAccountId(accountId);
        return this;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Status getAccountStatus() {
        return this.accountStatus;
    }

    public FintechAccount accountStatus(Status accountStatus) {
        this.setAccountStatus(accountStatus);
        return this;
    }

    public void setAccountStatus(Status accountStatus) {
        this.accountStatus = accountStatus;
    }

    public RegistrationStatus getRegistrationStatus() {
        return this.registrationStatus;
    }

    public FintechAccount registrationStatus(RegistrationStatus registrationStatus) {
        this.setRegistrationStatus(registrationStatus);
        return this;
    }

    public void setRegistrationStatus(RegistrationStatus registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public Double getAvailableBalance() {
        return this.availableBalance;
    }

    public FintechAccount availableBalance(Double availableBalance) {
        this.setAvailableBalance(availableBalance);
        return this;
    }

    public void setAvailableBalance(Double availableBalance) {
        this.availableBalance = availableBalance;
    }

    public Double getLedgerBalance() {
        return this.ledgerBalance;
    }

    public FintechAccount ledgerBalance(Double ledgerBalance) {
        this.setLedgerBalance(ledgerBalance);
        return this;
    }

    public void setLedgerBalance(Double ledgerBalance) {
        this.ledgerBalance = ledgerBalance;
    }

    public AccountOwner getAccountOwner() {
        return this.accountOwner;
    }

    public void setAccountOwner(AccountOwner accountOwner) {
        this.accountOwner = accountOwner;
    }

    public FintechAccount accountOwner(AccountOwner accountOwner) {
        this.setAccountOwner(accountOwner);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FintechAccount)) {
            return false;
        }
        return id != null && id.equals(((FintechAccount) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FintechAccount{" +
            "id=" + getId() +
            ", accountType='" + getAccountType() + "'" +
            ", accountId='" + getAccountId() + "'" +
            ", accountStatus='" + getAccountStatus() + "'" +
            ", registrationStatus='" + getRegistrationStatus() + "'" +
            ", availableBalance=" + getAvailableBalance() +
            ", ledgerBalance=" + getLedgerBalance() +
            "}";
    }
}
