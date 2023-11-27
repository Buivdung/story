package fa.hust.service;

import fa.hust.entities.Stories;
import fa.hust.enums.EStatusStory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StoriesService {
    Page<Stories> findAll(String param, Pageable pageable);
    Page<Stories> findAll(Pageable pageable);
    Page<Stories> findAllByStatus(EStatusStory status,Pageable pageable);

    Stories saveStory(Stories stories);
    List<Stories> findAll();
    Optional<Stories> findById(Long id);
    List<Stories> findByIds(List<Long> ids);

    void deleteStory(Stories stories);


    Optional<Stories> findByName(String name);

    Long count(String param);
}
