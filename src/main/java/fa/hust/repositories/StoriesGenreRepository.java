package fa.hust.repositories;

import fa.hust.entities.KeyStoriesGenre;
import fa.hust.entities.StoriesGenre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoriesGenreRepository extends JpaRepository<StoriesGenre, KeyStoriesGenre> {
    void deleteByStory_Id(Long id);
}
