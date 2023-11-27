package fa.hust.repositories;

import fa.hust.entities.KeyPaid;
import fa.hust.entities.Paid;
import fa.hust.entities.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PaidRepository extends JpaRepository<Paid, KeyPaid> {
    boolean existsByAccount_IdAndStory_Id(Long accountId,Long storyId);
    Optional<Paid> findDistinctByAccount_IdAndStory_Id(Long accountId, Long storyId);
    @Query("SELECT p FROM Paid  p WHERE p.account.id = ?1")
    Page<Paid> findAll(Long accountId, Pageable pageable);
}
