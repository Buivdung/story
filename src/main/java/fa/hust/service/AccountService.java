package fa.hust.service;

import fa.hust.entities.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    Optional<Account> findByEmail(String email);
    Optional<Account> findAccountByIdAndEmail(Long id,String email);

    Page<Account> findAll(String param, Pageable pageable);

    Account saveAccount(Account account);
    void deleteAccount(Account account);

    Optional<Account> findAccountById(Long id);
    List<Account> findAll();
    List<Account> findA();
}
