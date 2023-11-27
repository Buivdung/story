package fa.hust.repositories;

import fa.hust.entities.Stories;
import fa.hust.enums.EStatusStory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StoriesRepository extends JpaRepository<Stories, Long> {
    @Query("SELECT distinct s from Stories s " +
            "JOIN s.storiesGenres sg " +
            "where concat(s.name, s.author, s.createDate, s.status, sg.genre.name)" +
            "like %?1%")
    Page<Stories> findAll(String param, Pageable pageable);
    boolean existsByName(String name);

    Optional<Stories> findByName(String name);

    @Query("SELECT count(*) FROM Stories s " +
            "JOIN s.storiesGenres sg " +
            "where concat(s.name, s.author, s.createDate, s.status, sg.genre.name)" +
            "LIKE %?1%")
    Long count(String param);


    Page<Stories> findAllByStatus(EStatusStory status,Pageable pageable);
}
