package fa.hust.repositories;

import fa.hust.entities.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface GenreRepository extends JpaRepository<Genre,Long> {
    @Query("SELECT g FROM Genre g WHERE g.name LIKE %?1%")
    Page<Genre> findAll(String param, Pageable pageable);

    boolean existsByName(String name);


    List<Genre> findByIdNotIn(Collection<Long> id);
}
