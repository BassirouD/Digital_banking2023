package org.sid.ebankingback.repositories;

import org.sid.ebankingback.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {
    List<Customer> findByNameContaining(String keyword);

    /**
     * Ou
     */
    @Query("select c from Customer c where c.name like :kw")
    List<Customer> searchCustomer(@Param(value = "kw") String keyword);
}
