package com.victor.fintechaccountservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.victor.fintechaccountservice.domain.enumeration.Gender;
import com.victor.fintechaccountservice.domain.enumeration.Status;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A AccountOwner.
 */
@Entity
@Table(name = "account_owner")
public class AccountOwner implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @NotNull
    @Column(name = "user_reference", nullable = false)
    private String userReference;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @OneToMany(mappedBy = "accountOwner")
    @JsonIgnoreProperties(value = { "accountOwner" }, allowSetters = true)
    private Set<FintechAccount> fintechAccounts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AccountOwner id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public AccountOwner firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public AccountOwner lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return this.middleName;
    }

    public AccountOwner middleName(String middleName) {
        this.setMiddleName(middleName);
        return this;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getEmail() {
        return this.email;
    }

    public AccountOwner email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public AccountOwner password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public AccountOwner dateOfBirth(LocalDate dateOfBirth) {
        this.setDateOfBirth(dateOfBirth);
        return this;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getUserReference() {
        return this.userReference;
    }

    public AccountOwner userReference(String userReference) {
        this.setUserReference(userReference);
        return this;
    }

    public void setUserReference(String userReference) {
        this.userReference = userReference;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public AccountOwner phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return this.address;
    }

    public AccountOwner address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Gender getGender() {
        return this.gender;
    }

    public AccountOwner gender(Gender gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Status getStatus() {
        return this.status;
    }

    public AccountOwner status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<FintechAccount> getFintechAccounts() {
        return this.fintechAccounts;
    }

    public void setFintechAccounts(Set<FintechAccount> fintechAccounts) {
        if (this.fintechAccounts != null) {
            this.fintechAccounts.forEach(i -> i.setAccountOwner(null));
        }
        if (fintechAccounts != null) {
            fintechAccounts.forEach(i -> i.setAccountOwner(this));
        }
        this.fintechAccounts = fintechAccounts;
    }

    public AccountOwner fintechAccounts(Set<FintechAccount> fintechAccounts) {
        this.setFintechAccounts(fintechAccounts);
        return this;
    }

    public AccountOwner addFintechAccount(FintechAccount fintechAccount) {
        this.fintechAccounts.add(fintechAccount);
        fintechAccount.setAccountOwner(this);
        return this;
    }

    public AccountOwner removeFintechAccount(FintechAccount fintechAccount) {
        this.fintechAccounts.remove(fintechAccount);
        fintechAccount.setAccountOwner(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountOwner)) {
            return false;
        }
        return id != null && id.equals(((AccountOwner) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountOwner{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", middleName='" + getMiddleName() + "'" +
            ", email='" + getEmail() + "'" +
            ", password='" + getPassword() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", userReference='" + getUserReference() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", address='" + getAddress() + "'" +
            ", gender='" + getGender() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
