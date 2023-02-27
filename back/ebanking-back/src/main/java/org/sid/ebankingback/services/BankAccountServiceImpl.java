package org.sid.ebankingback.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.ebankingback.dtos.*;
import org.sid.ebankingback.entities.*;
import org.sid.ebankingback.enums.AccountSatus;
import org.sid.ebankingback.enums.OperationType;
import org.sid.ebankingback.exceptions.BalanceNotSufficientExceptions;
import org.sid.ebankingback.exceptions.CustomerNotFoundException;
import org.sid.ebankingback.mappers.BankAccountMapperImpl;
import org.sid.ebankingback.repositories.AccountOperationRepo;
import org.sid.ebankingback.repositories.BankAccountRepo;
import org.sid.ebankingback.repositories.CustomerRepo;
import org.sid.ebankingback.exceptions.BankAccountNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements IBankAccountService {
    private CustomerRepo customerRepo;
    private BankAccountRepo bankAccountRepo;
    private AccountOperationRepo accountOperationRepo;
    private BankAccountMapperImpl bankAccountMapper;


    /**
     * Logger log = LoggerFactory.getLogger(this.getClass().getName());
     * Remplacer par l'annotation @Slf4j
     */


    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new customer");
        Customer customer = bankAccountMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepo.save(customer);
        return bankAccountMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Updating customer");
        Customer customer = bankAccountMapper.fromCustomerDTO(customerDTO);
        Customer updatedCustomer = customerRepo.save(customer);
        return bankAccountMapper.fromCustomer(updatedCustomer);
    }

//    @Override
//    public BankAccount saveBankAccount(double initialBalance, String type, Long customerId) throws CustomerNotFoundException {
//
//    }

    @Override
    public BankAccountCurrentDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepo.findById(customerId).orElse(null);
        if (customer == null)
            throw new CustomerNotFoundException("Customer not found");

        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreateAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setCustomer(customer);
        currentAccount.setCurrency("DAL");
        currentAccount.setStatus(AccountSatus.CREATED);
        currentAccount.setOvertDraft(overDraft);
        CurrentAccount savedCurrentAccount = bankAccountRepo.save(currentAccount);
        return bankAccountMapper.fromCurrentAccount(savedCurrentAccount);
    }

    @Override
    public BankAccountSavingDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepo.findById(customerId).orElse(null);
        if (customer == null)
            throw new CustomerNotFoundException("Customer not found");

        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreateAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCustomer(customer);
        savingAccount.setStatus(AccountSatus.CREATED);
        savingAccount.setCurrency("EUR");
        savingAccount.setInterestRate(interestRate);
        SavingAccount savedSavingAccount = bankAccountRepo.save(savingAccount);
        return bankAccountMapper.fromSavingAccount(savedSavingAccount);
    }

    @Override
    public List<CustomerDTO> lisCustomers() {
        List<Customer> customers = customerRepo.findAll();
        /**
         * Programmation impérative
         */
        /**
         * List<CustomerDTO> customerDTOS = new ArrayList<>();
         *for (Customer customer : customers) {
         *CustomerDTO customerDTO = bankAccountMapper.fromCustomer(customer);
         *customerDTOS.add(customerDTO);
         *}
         **/
        /**
         * Programmtion fonctionnelle
         */
        List<CustomerDTO> customerDTOS = customers
                .stream()
                .map(customer ->
                        bankAccountMapper.fromCustomer(customer))
                .collect(Collectors.toList());

        return customerDTOS;
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepo
                .findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));

        if (bankAccount instanceof SavingAccount) {
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            return bankAccountMapper.fromSavingAccount(savingAccount);
        } else {
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
            return bankAccountMapper.fromCurrentAccount(currentAccount);
        }
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientExceptions {
        BankAccount bankAccount = bankAccountRepo
                .findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));

        if (bankAccount.getBalance() < amount)
            throw new BalanceNotSufficientExceptions("Balance not sufficient");

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepo.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepo.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepo
                .findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepo.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepo.save(bankAccount);
    }

    @Override
    public void tranfert(String customerIdSource, String customerIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientExceptions {
        debit(customerIdSource, amount, "Transfer to" + customerIdDestination);
        credit(customerIdDestination, amount, "transfer from" + customerIdSource);
    }

    @Override
    public List<BankAccountDTO> bankAccountList() {
        List<BankAccount> bankAccounts = bankAccountRepo.findAll();
        return bankAccounts
                .stream()
                .map(bankAccount -> {
                    if (bankAccount instanceof SavingAccount) {
                        SavingAccount savingAccount = (SavingAccount) bankAccount;
                        return bankAccountMapper.fromSavingAccount(savingAccount);
                    } else {
                        CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                        return bankAccountMapper.fromCurrentAccount(currentAccount);
                    }
                }).collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepo
                .findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        return bankAccountMapper.fromCustomer(customer);
    }


    @Override
    public void deleteCustomer(Long customerId) {
        customerRepo.deleteById(customerId);
    }

    @Override
    public List<AccountOperationDTO> accountOperationHistory(String accountId) {
        List<AccountOperation> accountOperations = accountOperationRepo.findByBankAccountId(accountId);
        return accountOperations.stream().map(accountOperation -> bankAccountMapper.fromAccountOperation(accountOperation)).collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepo.findById(accountId).orElse(null);

        if (bankAccount == null) throw new BankAccountNotFoundException("Account not found");

        Page<AccountOperation> accountOperations = accountOperationRepo.findByBankAccountIdOrderByOperationDateDesc(accountId, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        List<AccountOperationDTO> accountOperationDTOS = accountOperations.getContent().stream().map(accountOperation -> bankAccountMapper.fromAccountOperation(accountOperation)).collect(Collectors.toList());
        accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOS);
        accountHistoryDTO.setAccountID(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());

        return accountHistoryDTO;
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        List<Customer> customers = customerRepo.searchCustomer(keyword);
        List<CustomerDTO> customerDTOS = customers.stream().map(customer -> bankAccountMapper.fromCustomer(customer)).collect(Collectors.toList());

        return customerDTOS;
    }
}
