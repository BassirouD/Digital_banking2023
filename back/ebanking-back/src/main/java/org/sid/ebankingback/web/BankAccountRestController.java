package org.sid.ebankingback.web;

import lombok.AllArgsConstructor;
import org.sid.ebankingback.dtos.*;
import org.sid.ebankingback.exceptions.BalanceNotSufficientExceptions;
import org.sid.ebankingback.exceptions.BankAccountNotFoundException;
import org.sid.ebankingback.services.IBankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
public class BankAccountRestController {

    private IBankAccountService iBankAccountService;

    @GetMapping("/accounts/{accountId}")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
        return iBankAccountService.getBankAccount(accountId);
    }

    @GetMapping("/accounts")
    public List<BankAccountDTO> listAccount() {
        return iBankAccountService.bankAccountList();
    }

    @GetMapping("/accounts/{accountId}/operations")
    public List<AccountOperationDTO> getOperationsHistory(@PathVariable String accountId) {
        return iBankAccountService.accountOperationHistory(accountId);
    }

    @GetMapping("/accounts/{accountId}/pageOperations")
    public AccountHistoryDTO getAccountHistory(
            @PathVariable String accountId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size
    ) throws BankAccountNotFoundException {
        return iBankAccountService.getAccountHistory(accountId, page, size);
    }

    @PostMapping("accounts/debit")
    public DebitDTO debit(@RequestBody DebitDTO debitDTO) throws BankAccountNotFoundException, BalanceNotSufficientExceptions {
        this.iBankAccountService.debit(debitDTO.getAccountId(), debitDTO.getAmount(), debitDTO.getDescription());
        return debitDTO;
    }

    @PostMapping("accounts/credit")
    public CreditDTO credit(@RequestBody CreditDTO creditDTO) throws BankAccountNotFoundException {
        this.iBankAccountService.credit(creditDTO.getAccountId(), creditDTO.getAmount(), creditDTO.getDescription());
        return creditDTO;
    }

    @PostMapping("accounts/transfer")
    public void transfer(@RequestBody TransferRequestDTO transferRequestDTO) throws BankAccountNotFoundException, BalanceNotSufficientExceptions {
        this.iBankAccountService.tranfert(transferRequestDTO.getAccountIdSource(), transferRequestDTO.getAccountIDestination(), transferRequestDTO.getAmount());

    }


}
