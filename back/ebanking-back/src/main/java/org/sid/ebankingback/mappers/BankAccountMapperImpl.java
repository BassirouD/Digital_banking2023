package org.sid.ebankingback.mappers;

import org.sid.ebankingback.dtos.AccountOperationDTO;
import org.sid.ebankingback.dtos.BankAccountCurrentDTO;
import org.sid.ebankingback.dtos.BankAccountSavingDTO;
import org.sid.ebankingback.dtos.CustomerDTO;
import org.sid.ebankingback.entities.AccountOperation;
import org.sid.ebankingback.entities.CurrentAccount;
import org.sid.ebankingback.entities.Customer;
import org.sid.ebankingback.entities.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapperImpl {
    public CustomerDTO fromCustomer(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        /**
         customerDTO.setId(customer.getId());
         customerDTO.setName(customer.getName());
         customerDTO.setEmail(customer.getEmail());
         **/
        return customerDTO;
    }

    public Customer fromCustomerDTO(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }

    public BankAccountSavingDTO fromSavingAccount(SavingAccount savingAccount) {
        BankAccountSavingDTO bankAccountSavingDTO = new BankAccountSavingDTO();
        BeanUtils.copyProperties(savingAccount, bankAccountSavingDTO);
        bankAccountSavingDTO.setCustomerDTO(fromCustomer(savingAccount.getCustomer()));
        bankAccountSavingDTO.setType(savingAccount.getClass().getSimpleName());
        return bankAccountSavingDTO;
    }

    public SavingAccount fromSavingAccountDTO(BankAccountSavingDTO bankAccountSavingDTO) {
        SavingAccount savingAccount = new SavingAccount();
        BeanUtils.copyProperties(bankAccountSavingDTO, savingAccount);
        savingAccount.setCustomer(fromCustomerDTO(bankAccountSavingDTO.getCustomerDTO()));
        return savingAccount;
    }

    public BankAccountCurrentDTO fromCurrentAccount(CurrentAccount currentAccount) {
        BankAccountCurrentDTO bankAccountCurrentDTO = new BankAccountCurrentDTO();
        BeanUtils.copyProperties(currentAccount, bankAccountCurrentDTO);
        bankAccountCurrentDTO.setCustomerDTO(fromCustomer(currentAccount.getCustomer()));
        bankAccountCurrentDTO.setType(currentAccount.getClass().getSimpleName());
        return bankAccountCurrentDTO;
    }

    public CurrentAccount fromCurrentAccountDTO(BankAccountCurrentDTO bankAccountCurrentDTO) {
        CurrentAccount currentAccount = new CurrentAccount();
        BeanUtils.copyProperties(bankAccountCurrentDTO, currentAccount);
        currentAccount.setCustomer(fromCustomerDTO(bankAccountCurrentDTO.getCustomerDTO()));
        return currentAccount;
    }

    public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation) {
        AccountOperationDTO accountOperationDTO = new AccountOperationDTO();
        BeanUtils.copyProperties(accountOperation, accountOperationDTO);
        return accountOperationDTO;
    }

    public AccountOperation fromAccountOperationDTO(AccountOperationDTO accountOperationDTO) {
        AccountOperation accountOperation = new AccountOperation();
        BeanUtils.copyProperties(accountOperationDTO, accountOperation);
        return accountOperation;
    }

}
