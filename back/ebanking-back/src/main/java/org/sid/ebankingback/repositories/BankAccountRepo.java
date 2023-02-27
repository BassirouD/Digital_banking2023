package org.sid.ebankingback.repositories;

import org.sid.ebankingback.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//@Repository
public interface BankAccountRepo extends JpaRepository<BankAccount, String> {
}
