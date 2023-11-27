package fa.hust.repositories;

import fa.hust.entities.Chapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    Long countChapterByStories_Id(Long id);

    @Query("SELECT c FROM Chapter c WHERE c.stories.id = ?1 AND concat(c.number, c.createDate, c.title) LIKE %?2%")
    Page<Chapter> findAll(Long id, String param, Pageable pageable);
    @Query("SELECT c FROM Chapter c WHERE c.stories.id = ?1 ")
    Page<Chapter> findAll(Long id,  Pageable pageable);

    @Query("SELECT c FROM Chapter c WHERE c.stories.id = ?1 AND c.id = ?2")
    Optional<Chapter> findById(Long storyId, Long id);

    @Query("SELECT c.id FROM Chapter c WHERE c.stories.id = ?1")
    List<Long> getIds(Long storyId, Sort sort);
    @Query("SELECT DISTINCT c.stories.id FROM Chapter c")
    List<Long> getStoryIds();

    List<Chapter> findChapterByStories_Id(Long storyId);

    Optional<Chapter> findTopByStories_IdOrderByNumberDesc(Long id);


}
