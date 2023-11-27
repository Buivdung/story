package fa.hust.controllers;



import fa.hust.entities.Chapter;
import fa.hust.enums.ETypeFile;
import fa.hust.entities.Stories;
import fa.hust.req.ChapterReq;
import fa.hust.req.FileUpload;
import fa.hust.req.SearchReq;
import fa.hust.service.ChapterService;
import fa.hust.service.StoriesService;
import fa.hust.utils.FileUtil;
import fa.hust.utils.SearchUtil;
import jakarta.servlet.annotation.MultipartConfig;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@MultipartConfig
public class ChapterController {
    private final ChapterService chapterService;
    private final StoriesService storiesService;
    private final FileUtil fileUtil;
    private final ModelMapper modelMapper;
    private final SearchUtil searchUtil;

    @GetMapping("/{storyId}/chapters")
    public String getChapters(@ModelAttribute ChapterReq chapterReq,
                              @ModelAttribute SearchReq searchReq,
                              @PathVariable Long storyId,
                              Model model) {
        searchReq = searchUtil.convertSearchReq(searchReq);
        Pageable pageable = PageRequest.of(searchReq.getPageNumber() - 1, searchReq.getPageSize(), Sort.by("number").ascending());

        Page<Chapter> chapters = chapterService.findAll(storyId, searchReq.getParam(), pageable);
        model.addAttribute("searchResp", searchUtil.setSearch(searchReq, chapters.getTotalPages()));
        model.addAttribute("chapters", chapters);
        model.addAttribute("storyId", storyId);
        return "admin/chapter";
    }

    @PostMapping("/{storyId}/chapters")
    public String postChapters(@ModelAttribute ChapterReq chapterReq,
                               @ModelAttribute SearchReq searchReq,
                               @PathVariable Long storyId,
                               Model model) throws IOException {
        FileUpload fileUpload = fileUtil.checkFile(chapterReq.getContent(), ETypeFile.CHAPTER);
        Chapter chapter = modelMapper.map(chapterReq, Chapter.class);
        Stories stories = storiesService.findById(storyId).orElseThrow();
        String fileName = chapter.getNumber() + ".txt";
        fileUpload.setFileName(fileName);
        chapter.setContent(fileName);
        chapter.setStories(stories);
        chapterService.saveChapter(chapter);
        fileUtil.saveFile(fileUpload, storyId);
        model.addAttribute("searchResp", searchReq);
        model.addAttribute("storyId", storyId);
        return getChapters(chapterReq, searchReq, storyId, model);
    }

    @GetMapping("/{storyId}/chapter/{id}")
    public String getChapter(@PathVariable Long storyId, @PathVariable Long id, Model model) throws IOException {
        Chapter chapter = chapterService.findById(storyId, id).orElseThrow();
        List<Long> ids = chapterService.getIds(storyId, Sort.by("number").ascending());
        List<String> lines = fileUtil.loadText(chapter.getContent(), storyId, ETypeFile.CHAPTER);
        model.addAttribute("ids", ids);
        model.addAttribute("lines", lines);
        model.addAttribute("chapter", chapter);
        return "admin/chapterDetail";
    }

    @PostMapping("/{storyId}/chapter/{id}")
    public String postChapter(@ModelAttribute ChapterReq chapterReq,
                              @PathVariable Long storyId,
                              @PathVariable Long id) {
        Chapter chapter = chapterService.findById(id).orElseThrow();
        modelMapper.map(chapterReq, chapter);
        chapter.getStories().setId(storyId);
        chapterService.saveChapter(chapter);
        return "redirect:/admin/" + storyId + "/chapter/" + id;
    }

    @PostMapping("/{storyId}/content/{id}")
    public String putContent(@ModelAttribute ChapterReq chapterReq,
                             @PathVariable Long storyId,
                             @PathVariable Long id) throws IOException {
        Chapter chapter = chapterService.findById(id).orElseThrow();
        FileUpload fileUpload = fileUtil.checkFile(chapterReq.getContent(), ETypeFile.CHAPTER);
        fileUpload.setFileName(chapter.getContent());
        fileUtil.saveFile(fileUpload, storyId);
        return "redirect:/admin/" + storyId + "/chapter/" + id;
    }

    @GetMapping("/chapter/total/{id}")
    @ResponseBody
    public Long getChapterCount(@PathVariable Long id) {
        Long total = chapterService.countChapterByStoryId(id);
        return total + 1L;
    }


    @GetMapping("/{storyId}/chapters/delete/{id}")
    public String deleteChapter(@PathVariable Long id, @PathVariable Long storyId) {
        Chapter chapter = chapterService.findById(id).orElseThrow();
        chapterService.deleteChapter(chapter);
        return "redirect:/admin/" + storyId + "/chapters";
    }
}
