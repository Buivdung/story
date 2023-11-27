package fa.hust.controllers;

import fa.hust.enums.EStatusStory;
import fa.hust.enums.ETypeFile;
import fa.hust.req.FileUpload;
import fa.hust.req.SearchReq;
import fa.hust.req.StoryReq;
import fa.hust.entities.*;
import fa.hust.service.GenreService;
import fa.hust.service.StoriesService;
import fa.hust.utils.FileUtil;
import fa.hust.utils.SearchUtil;
import jakarta.servlet.annotation.MultipartConfig;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@MultipartConfig
public class StoryController {
    private final StoriesService storiesService;
    private final SearchUtil searchUtil;
    private final ModelMapper modelMapper;
    private final GenreService genreService;
    private final FileUtil fileUtil;
    private static final String ERR_NOT_STORY = "Thao tác lỗi, truyện không tồn tại";


    @GetMapping("/stories")
    public String getStory(@ModelAttribute SearchReq searchReq,
                           @ModelAttribute StoryReq storyReq,
                           Model model) {
        searchReq = searchUtil.convertSearchReq(searchReq);
        Pageable pageable = PageRequest.of(searchReq.getPageNumber() - 1, searchReq.getPageSize());
        Page<Stories> stories = storiesService.findAll(searchReq.getParam(), pageable);
        model.addAttribute("searchResp", searchUtil.setSearch(searchReq, stories.getTotalPages()));
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("stories", stories);
        return "admin/story";
    }


    @PostMapping("/stories")
    public String postStory(@ModelAttribute StoryReq storyReq) throws IOException {
        Stories story = modelMapper.map(storyReq, Stories.class);
        FileUpload thumbnail = fileUtil.checkFile(storyReq.getThumbnail(), ETypeFile.THUMBNAIL);
        FileUpload introduction = fileUtil.checkFile(storyReq.getIntroduction(), ETypeFile.INTRODUCTION);
        story.setStatus(EStatusStory.PENDING);
        story.setThumbnail(thumbnail.getFileName());
        story.setIntroduction(introduction.getFileName());
        story = storiesService.saveStory(story);
        fileUtil.saveFile(thumbnail, story.getId());
        fileUtil.saveFile(introduction, story.getId());
        return "redirect:/admin/stories";
    }

    @GetMapping("/stories/delete/{id}")
    public String deleteStory(@PathVariable Long id) {
        Stories story = storiesService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ERR_NOT_STORY));
        storiesService.deleteStory(story);
        return "redirect:/admin/stories";
    }

    @GetMapping("/story/{id}")
    public String getStory(@PathVariable Long id,
                           @ModelAttribute SearchReq searchReq,
                           @ModelAttribute StoryReq storyReq,
                           Model model) throws IOException {
        Stories story = storiesService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ERR_NOT_STORY));
        List<Long> ids = story.getStoriesGenres().stream().map(x -> x.getGenre().getId()).toList();
        model.addAttribute("lines", fileUtil.loadText(story.getIntroduction(), story.getId(), ETypeFile.INTRODUCTION));
        model.addAttribute("genres", genreService.findByIdNotIn(ids));
        model.addAttribute("story", story);
        return "admin/storyDetail";
    }

    @PostMapping("/story/{id}")
    public String putStory(@PathVariable Long id, @ModelAttribute StoryReq storyReq) {
        Stories story = storiesService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ERR_NOT_STORY));
        Stories story2 = storiesService.findByName(storyReq.getName()).orElse(new Stories());
        if(!Objects.equals(story.getId(), story2.getId())){
            throw new IllegalArgumentException("Truyen da ton tai");
        }
        modelMapper.map(storyReq, story);
        storiesService.saveStory(story);
        return "redirect:/admin/story/" + id;
    }

    @PostMapping("/story/thumbnail/{id}")
    public String putThumbnail(@PathVariable Long id,
                               @RequestParam MultipartFile thumbnail) throws IOException {
        Stories story = storiesService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ERR_NOT_STORY));
        FileUpload fileUpload = fileUtil.checkFile(thumbnail, ETypeFile.THUMBNAIL);
        fileUpload.setFileName(story.getThumbnail());
        fileUtil.saveFile(fileUpload, story.getId());
        return "redirect:/admin/story/" + id;
    }

    @PostMapping("/story/introduction/{id}")
    public String putIntroduction(@PathVariable Long id,
                                  @RequestParam MultipartFile introduction) throws IOException {
        Stories story = storiesService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ERR_NOT_STORY));
        FileUpload fileUpload = fileUtil.checkFile(introduction, ETypeFile.INTRODUCTION);
        fileUpload.setFileName(story.getIntroduction());
        fileUtil.saveFile(fileUpload, story.getId());
        return "redirect:/admin/story/" + id;
    }

}
