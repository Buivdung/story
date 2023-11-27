package fa.hust.service;

import fa.hust.dto.TotalMoney;
import fa.hust.entities.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PaymentService {
    Payment savePayment(Payment payment);
    Optional<Payment> findById(Long id);

    Page<Payment> findAll(Long accountId, Pageable pageable);
    Page<Payment> findAll(String param, Pageable pageable);
    Long totalAmount();
    List<TotalMoney> getT();
}
