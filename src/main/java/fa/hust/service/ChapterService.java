package fa.hust.service;

import fa.hust.entities.Chapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface ChapterService {
    Long countChapterByStoryId(Long id);

    Chapter saveChapter(Chapter chapter);

    Page<Chapter> findAll(Long id, String param, Pageable pageable);
    Page<Chapter> findAll(Long id,  Pageable pageable);


    void deleteChapter(Chapter chapter);

    Optional<Chapter> findById(Long id);

    Optional<Chapter> findById(Long storyId, Long id);

    List<Long> getIds(Long storyId, Sort sort);
    List<Chapter> findTopChapterByStoryId( );


}
