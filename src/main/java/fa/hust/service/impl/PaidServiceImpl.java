package fa.hust.service.impl;


import fa.hust.entities.Paid;
import fa.hust.repositories.PaidRepository;
import fa.hust.service.PaidService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaidServiceImpl implements PaidService {
    final private PaidRepository paidRepository;


    @Override
    public boolean existsByAccountAndStory(Long accountId, Long storyId) {
        return paidRepository.existsByAccount_IdAndStory_Id(accountId,storyId);
    }

    @Override
    public Optional<Paid> findByAccountIdAndStoryId(Long accountId, Long storyId) {
        return paidRepository.findDistinctByAccount_IdAndStory_Id(accountId,storyId);
    }

    @Override
    @Transactional
    public Paid savePaid(Paid paid) {
        return paidRepository.save(paid);
    }

    @Override
    public Page<Paid> findAll(Long accountId, Pageable pageable) {
        return paidRepository.findAll(accountId,pageable);
    }
}
