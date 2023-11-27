package fa.hust.repositories;

import fa.hust.dto.TotalMoney;
import fa.hust.entities.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment,Long> {

    @Query("SELECT p FROM Payment  p WHERE p.account.id = ?1")
    Page<Payment> findAll(Long accountId, Pageable pageable);
    @Query("SELECT p FROM Payment  p " +
            "WHERE concat(p.account.information.fullName, p.account.email, p.story.name, p.status, p.amount, p.createDate) " +
            "LIKE %?1%")
    Page<Payment> findAll(String param, Pageable pageable);

    @Query("select sum(p.amount) from Payment p where p.status = 'SUCCESS'")
    Long totalAmount();

    @Query("SELECT new fa.hust.dto.TotalMoney(p.story.id,count (p.story.id), sum (p.amount) ) " +
            "from Payment p " +
            "where p.status = 'SUCCESS'" +
            "group by p.story.id " +
            "order by sum (p.amount) desc " +
            "limit 10")
    List<TotalMoney> getT();

}
