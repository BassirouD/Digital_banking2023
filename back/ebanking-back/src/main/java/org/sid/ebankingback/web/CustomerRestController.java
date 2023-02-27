package org.sid.ebankingback.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.ebankingback.dtos.CustomerDTO;
import org.sid.ebankingback.exceptions.CustomerNotFoundException;
import org.sid.ebankingback.services.IBankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@CrossOrigin("*")
public class CustomerRestController {
    private IBankAccountService iBankAccountService;

    @GetMapping("/customers")
    public List<CustomerDTO> customers() {
        return iBankAccountService.lisCustomers();
    }

    @GetMapping("/customers/search")
    public List<CustomerDTO> searchCustomers(@RequestParam(name = "keyword", defaultValue = "") String keyword) {
        return iBankAccountService.searchCustomers("%" + keyword + "%");
    }


    @GetMapping("/customers/{id}")
    public CustomerDTO getCustomer(@PathVariable(value = "id") Long customerId) throws CustomerNotFoundException {
        return iBankAccountService.getCustomer(customerId);
    }

    @PostMapping("/customers")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        iBankAccountService.saveCustomer(customerDTO);
        return customerDTO;
    }

    @PutMapping("/customers/{customerId}")
    public CustomerDTO updateCustomer(@PathVariable Long customerId, @RequestBody CustomerDTO customerDTO) {
        customerDTO.setId(customerId);
        iBankAccountService.updateCustomer(customerDTO);
        return customerDTO;
    }

    @DeleteMapping("/customers/{id}")
    public void deleCustomer(@PathVariable Long id) {
        iBankAccountService.deleteCustomer(id);
    }


}
