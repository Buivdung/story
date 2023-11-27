package fa.hust.service.impl;


import fa.hust.entities.*;
import fa.hust.enums.EStatusStory;
import fa.hust.repositories.GenreRepository;
import fa.hust.repositories.StoriesGenreRepository;
import fa.hust.repositories.StoriesRepository;
import fa.hust.service.StoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoriesServiceImpl implements StoriesService {
    private final StoriesRepository storiesRepository;
    private final GenreRepository genreRepository;
    private final StoriesGenreRepository storiesGenreRepository;

    @Override
    public Page<Stories> findAll(String param, Pageable pageable) {
        return storiesRepository.findAll(param, pageable);
    }

    @Override
    public Page<Stories> findAll(Pageable pageable) {
        return storiesRepository.findAll(pageable);
    }

    @Override
    public Page<Stories> findAllByStatus(EStatusStory status, Pageable pageable) {
        return storiesRepository.findAllByStatus(status,pageable);
    }

    @Override
    @Transactional
    public Stories saveStory(Stories stories) {
        List<StoriesGenre> storiesGenres = new ArrayList<>();
        stories.getGenreId().forEach(id -> {
            Genre genre = genreRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Không tồn tại thể loại này"));
            storiesGenres.add(StoriesGenre.builder().genre(genre).build());
        });
        if (stories.getId() == null && storiesRepository.existsByName(stories.getName())) {
            throw new IllegalArgumentException("Tên truyện đã tồn tại");
        }
        final Stories story = storiesRepository.save(stories);
        storiesGenreRepository.deleteByStory_Id(story.getId());
        storiesGenres.forEach(x -> {
            x.setStory(story);
            x.setId(KeyStoriesGenre.builder()
                    .storyId(story.getId())
                    .genreId(x.getGenre().getId())
                    .build());
        });
        storiesGenreRepository.saveAll(storiesGenres);
        return story;
    }

    @Override
    public List<Stories> findAll() {
        return storiesRepository.findAll();
    }

    @Override
    public Optional<Stories> findById(Long id) {
        return storiesRepository.findById(id);
    }

    @Override
    public List<Stories> findByIds(List<Long> ids) {
        return storiesRepository.findAllById(ids);
    }

    @Override
    @Transactional
    public void deleteStory(Stories stories) {
        storiesRepository.delete(stories);
    }

    @Override
    public Optional<Stories> findByName(String name) {

        return storiesRepository.findByName(name);
    }

    @Override
    public Long count(String param) {
        return storiesRepository.count(param);
    }


}
