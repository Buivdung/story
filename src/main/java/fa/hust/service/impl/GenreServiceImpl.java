package fa.hust.service.impl;


import fa.hust.entities.Account;
import fa.hust.entities.Genre;
import fa.hust.repositories.GenreRepository;
import fa.hust.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    final private GenreRepository genreRepository;

    @Override
    @Transactional
    public Genre saveGenre(Genre genre) {
        if (genreRepository.existsByName(genre.getName())){
            throw new IllegalArgumentException("Thể loại đã tồn tại");
        }
        return genreRepository.save(genre);
    }

    @Override
    public Page<Genre> findAll(String param, Pageable pageable) {
        return genreRepository.findAll(param, pageable);
    }

    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    public Optional<Genre> findGenreById(Long id) {
        return genreRepository.findById(id);
    }

    @Override
    public void deleteGenre(Genre genre) {
        genreRepository.delete(genre);
    }

    @Override
    public List<Genre> findByIdNotIn(Collection<Long> id) {
        return genreRepository.findByIdNotIn(id);
    }
}
