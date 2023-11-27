package fa.hust.service.impl;

import fa.hust.entities.Account;
import fa.hust.repositories.AccountRepository;
import fa.hust.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Override
    public Optional<Account> findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    @Override
    public Optional<Account> findAccountByIdAndEmail(Long id,String email) {
        return accountRepository.findAccountByIdAndEmail(id,email);
    }

    @Override
    public Page<Account> findAll(String param, Pageable pageable) {
        return accountRepository.findAll(param, pageable);
    }

    @Override
    @Transactional
    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public void deleteAccount(Account account) {
        accountRepository.delete(account);
    }

    @Override
    public Optional<Account> findAccountById(Long id) {
        return accountRepository.findById(id);
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public List<Account> findA() {
        return accountRepository.findA();
    }
}
