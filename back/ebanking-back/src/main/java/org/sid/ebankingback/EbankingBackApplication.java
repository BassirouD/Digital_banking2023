package org.sid.ebankingback;

import org.sid.ebankingback.dtos.BankAccountCurrentDTO;
import org.sid.ebankingback.dtos.BankAccountDTO;
import org.sid.ebankingback.dtos.BankAccountSavingDTO;
import org.sid.ebankingback.dtos.CustomerDTO;
import org.sid.ebankingback.entities.*;
import org.sid.ebankingback.enums.AccountSatus;
import org.sid.ebankingback.enums.OperationType;
import org.sid.ebankingback.exceptions.BalanceNotSufficientExceptions;
import org.sid.ebankingback.exceptions.BankAccountNotFoundException;
import org.sid.ebankingback.exceptions.CustomerNotFoundException;
import org.sid.ebankingback.repositories.AccountOperationRepo;
import org.sid.ebankingback.repositories.BankAccountRepo;
import org.sid.ebankingback.repositories.CustomerRepo;
import org.sid.ebankingback.services.IBankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankingBackApplication.class, args);
    }

    @Bean
    CommandLineRunner start(CustomerRepo customerRepo,
                            BankAccountRepo bankAccountRepo,
                            AccountOperationRepo accountOperationRepo,
                            IBankAccountService iBankAccountService
    ) {
        return args -> {
            /**
             Stream.of("Aliou", "Bassirou", "Aicha").forEach(name -> {
             Customer customer = new Customer();
             customer.setName(name);
             customer.setEmail(name + "@mail.com");
             customerRepo.save(customer);
             });
             customerRepo.findAll().forEach(customer -> {
             CurrentAccount currentAccount = new CurrentAccount();
             currentAccount.setId(UUID.randomUUID().toString());
             currentAccount.setCurrency("CFA");
             currentAccount.setBalance(Math.random() * 9000);
             currentAccount.setCreateAt(new Date());
             currentAccount.setStatus(AccountSatus.CREATED);
             currentAccount.setCustomer(customer);
             currentAccount.setOvertDraft(9000);
             bankAccountRepo.save(currentAccount);

             SavingAccount savingAccount = new SavingAccount();
             savingAccount.setId(UUID.randomUUID().toString());
             savingAccount.setCurrency("SAD");
             savingAccount.setBalance(Math.random() * 9000);
             savingAccount.setCreateAt(new Date());
             savingAccount.setStatus(AccountSatus.CREATED);
             savingAccount.setCustomer(customer);
             savingAccount.setInterestRate(5.5);
             bankAccountRepo.save(savingAccount);
             });

             bankAccountRepo.findAll().forEach(bankAccount -> {
             for (int i = 0; i < 10; i++) {
             AccountOperation accountOperation = new AccountOperation();
             accountOperation.setOperationDate(new Date());
             accountOperation.setAmount(Math.random() * 1000);
             accountOperation.setType(Math.random() > 0.5 ? OperationType.DEBIT : OperationType.CREDIT);
             accountOperation.setBankAccount(bankAccount);
             accountOperationRepo.save(accountOperation);
             }
             });
             **/
            Stream.of("koula", "saliou", "dado")
                    .forEach(name -> {
                        CustomerDTO customer = new CustomerDTO();
                        customer.setName(name);
                        customer.setEmail(name + "@gmail.com");
                        iBankAccountService.saveCustomer(customer);
                    });
            iBankAccountService.lisCustomers().forEach(customer -> {
                try {
                    iBankAccountService.saveCurrentBankAccount(Math.random() * 5000, 800, customer.getId());
                    iBankAccountService.saveSavingBankAccount(Math.random() * 4000, 5.5, customer.getId());
                    List<BankAccountDTO> bankAccounts = iBankAccountService.bankAccountList();
                    for (BankAccountDTO bankAccount : bankAccounts) {
                        for(int i =0; i<10;i++){
                            String accountId;
                            if (bankAccount instanceof BankAccountSavingDTO) {
                                accountId = ((BankAccountSavingDTO) bankAccount).getId();
                            } else {
                                accountId = ((BankAccountCurrentDTO) bankAccount).getId();
                            }
                            iBankAccountService.credit(accountId, 1000 + Math.random() * 15000, "Credit");
                            iBankAccountService.debit(accountId, 100 + Math.random() + 500, "Debit");
                        }

                    }

                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                } catch (BankAccountNotFoundException | BalanceNotSufficientExceptions e) {
                    throw new RuntimeException(e);
                }
            });
        };
    }

}
