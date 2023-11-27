package fa.hust.service.impl;


import fa.hust.repositories.InformationRepository;
import fa.hust.service.InformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InformationServiceImpl implements InformationService {
    final private InformationRepository informationRepository;
}
