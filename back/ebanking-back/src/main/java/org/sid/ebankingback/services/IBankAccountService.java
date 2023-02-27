package org.sid.ebankingback.services;

import org.sid.ebankingback.dtos.*;
import org.sid.ebankingback.entities.BankAccount;
import org.sid.ebankingback.exceptions.BalanceNotSufficientExceptions;
import org.sid.ebankingback.exceptions.BankAccountNotFoundException;
import org.sid.ebankingback.exceptions.CustomerNotFoundException;

import java.util.List;

public interface IBankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    //    BankAccount saveBankAccount(double initialBalance, String type, Long customerId) throws CustomerNotFoundException;
    BankAccountCurrentDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;

    BankAccountSavingDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;

    /**
     * Apres usage des DTO, on retourne une liste de CustomerDTO
     * List<Customer> lisCustomers();
     *
     * @return
     */
    List<CustomerDTO> lisCustomers();

    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;

    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientExceptions;

    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;

    void tranfert(String customerIdSource, String customerIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientExceptions;

    List<BankAccountDTO> bankAccountList();

    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long customerId);

    List<AccountOperationDTO> accountOperationHistory(String accountId);

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;

    List<CustomerDTO> searchCustomers(String keyword);
}
