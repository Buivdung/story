package fa.hust.service;

import fa.hust.entities.Account;
import fa.hust.entities.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface GenreService {
    Genre saveGenre(Genre genre);

    Page<Genre> findAll(String param, Pageable pageable);
    List<Genre> findAll();

    Optional<Genre> findGenreById(Long id);

    void deleteGenre(Genre genre);
    List<Genre> findByIdNotIn(Collection<Long> id);
}
