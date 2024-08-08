package com.hnt.accounts.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "accounts")
public class Account extends BaseEntity{
    @Id
    private Long accountNumber;
    private Long customerId;
    private String accountType;
    private String branchAddress;

    @Column(name = "communication_sw")
    private Boolean communicationSw;
}
