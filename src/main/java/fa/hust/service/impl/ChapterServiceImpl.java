package fa.hust.service.impl;


import fa.hust.entities.Chapter;
import fa.hust.entities.Stories;
import fa.hust.repositories.ChapterRepository;
import fa.hust.repositories.StoriesRepository;
import fa.hust.service.ChapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChapterServiceImpl implements ChapterService {
    private final ChapterRepository chapterRepository;
    private final StoriesRepository storiesRepository;

    @Override
    public Long countChapterByStoryId(Long id) {
        return chapterRepository.countChapterByStories_Id(id);
    }

    @Override
    @Transactional
    public Chapter saveChapter(Chapter chapter) {
        chapter = chapterRepository.save(chapter);
        Long total = chapterRepository.countChapterByStories_Id(chapter.getStories().getId());
        Stories stories = storiesRepository.findById(chapter.getStories().getId()).orElseThrow();
        stories.setChapterTotal(total);
        return chapter;
    }

    @Override
    public Page<Chapter> findAll(Long id, String param, Pageable pageable) {
        return chapterRepository.findAll(id, param, pageable);
    }

    @Override
    public Page<Chapter> findAll(Long id, Pageable pageable) {
        return chapterRepository.findAll(id, pageable);
    }

    @Override
    @Transactional
    public void deleteChapter(Chapter chapter) {
        chapterRepository.delete(chapter);
        Long total = chapterRepository.countChapterByStories_Id(chapter.getStories().getId());
        Stories stories = storiesRepository.findById(chapter.getStories().getId()).orElseThrow();
        stories.setChapterTotal(total);
    }

    @Override
    public Optional<Chapter> findById(Long id) {
        return chapterRepository.findById(id);
    }

    @Override
    public Optional<Chapter> findById(Long storyId, Long id) {
        return chapterRepository.findById(storyId, id);
    }

    @Override
    public List<Long> getIds(Long storyId, Sort sort) {
        return chapterRepository.getIds(storyId, sort);
    }



    @Override
    public List<Chapter> findTopChapterByStoryId() {
        List<Long> ids = chapterRepository.getStoryIds();
        return ids.stream().
                map(i -> chapterRepository.findTopByStories_IdOrderByNumberDesc(i).orElseThrow()).toList();
    }
}
