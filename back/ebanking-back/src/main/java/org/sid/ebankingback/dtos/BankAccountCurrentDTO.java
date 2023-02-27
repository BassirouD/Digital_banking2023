package org.sid.ebankingback.dtos;

import lombok.Data;
import org.sid.ebankingback.enums.AccountSatus;

import java.util.Date;

@Data
public class BankAccountCurrentDTO extends BankAccountDTO{
    private String id;
    private double balance;
    private Date createAt;
    private String currency;
    private AccountSatus status;
    private CustomerDTO customerDTO;
    private double overDraft;
}
