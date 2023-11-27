package fa.hust.service;

import fa.hust.entities.Paid;
import fa.hust.entities.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PaidService {
    boolean existsByAccountAndStory(Long accountId, Long storyId);
    Optional<Paid> findByAccountIdAndStoryId(Long accountId, Long storyId);
    Paid savePaid(Paid paid);
    Page<Paid> findAll(Long accountId, Pageable pageable);

}
