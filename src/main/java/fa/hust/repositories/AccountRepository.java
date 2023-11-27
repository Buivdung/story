package fa.hust.repositories;

import fa.hust.entities.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);

    @Query("SELECT a from Account a where " +
            "concat(a.information.fullName, a.information.phoneNumber, a.information.gender," +
            "a.information.dob, a.information.address, a.information.createDate)" +
            "like %?1%")
    Page<Account> findAll(String value,Pageable pageable);

    @Query("SELECT a FROM Account a " +
            "join a.payments p " +
            "where p.status = 'SUCCESS'" +
            "order by p.id desc " +
            "limit 10")
    List<Account> findA();


    Optional<Account> findAccountByIdAndEmail(Long id,String email);
}
