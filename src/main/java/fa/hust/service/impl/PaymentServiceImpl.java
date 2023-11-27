package fa.hust.service.impl;


import fa.hust.dto.TotalMoney;
import fa.hust.entities.Payment;
import fa.hust.repositories.PaymentRepository;
import fa.hust.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public Optional<Payment> findById(Long id) {
        return paymentRepository.findById(id);
    }

    @Override
    public Page<Payment> findAll(Long accountId, Pageable pageable) {
        return paymentRepository.findAll(accountId, pageable);
    }

    @Override
    public Page<Payment> findAll(String param, Pageable pageable) {
        return paymentRepository.findAll(param,pageable);
    }

    @Override
    public Long totalAmount() {
        return paymentRepository.totalAmount();
    }

    @Override
    public List<TotalMoney> getT() {
        return paymentRepository.getT();
    }
}
