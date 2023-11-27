package fa.hust.controllers;

import fa.hust.entities.*;
import fa.hust.enums.ERoles;
import fa.hust.enums.EStatusStory;
import fa.hust.enums.ETypeFile;
import fa.hust.req.CommentReq;
import fa.hust.req.SearchReq;
import fa.hust.service.*;
import fa.hust.utils.FileUtil;
import fa.hust.utils.SearchUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;


import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RequiredArgsConstructor
@Controller
public class IndexController {


    private final ChapterService chapterService;
    private final SearchUtil searchUtil;
    private final StoriesService storiesService;
    private final GenreService genreService;
    private final FileUtil fileUtil;
    private final AccountService accountService;
    private final ModelMapper modelMapper;
    private final CommentService commentService;
    private final PaidService paidService;


    @GetMapping("/")
    public String getIndex(@RequestParam(required = false) Integer size,
                           Model model,
                           HttpServletRequest req) {
        setSession(req);
        size = size == null ? 20 : size;
        Pageable pageable = PageRequest.of(0, size);
        model.addAttribute("chapters", chapterService.findTopChapterByStoryId());
        model.addAttribute("stories", storiesService.findAll(pageable));
        model.addAttribute("storiesFull", storiesService.findAllByStatus(EStatusStory.FULL, PageRequest.of(0, 20)));
        return "ui/home";
    }

    @GetMapping("/story/{id}")
    public String story(@PathVariable Long id,
                        @RequestParam(required = false) Integer sizeCmt,
                        @ModelAttribute SearchReq searchReq,
                        Model model) throws IOException {
        Stories story = storiesService.findById(id).orElseThrow();
        sizeCmt = sizeCmt == null ? 15 : sizeCmt;
        searchReq = searchUtil.convertSearchReq(searchReq);
        Pageable pageable = PageRequest.of(searchReq.getPageNumber() - 1, 50,
                Sort.by("number").ascending());
        Pageable pageable1 = PageRequest.of(0, sizeCmt, Sort.by("id").descending());
        Page<Comment> comments = commentService.getComment(id, pageable1);
        Page<Chapter> chapters = chapterService.findAll(id, pageable);
        List<String> lines = fileUtil.loadText(story.getIntroduction(), id, ETypeFile.INTRODUCTION);
        Account account = getPrincipal();
        boolean isPaid = paidService.existsByAccountAndStory(account.getId(), story.getId());
        model.addAttribute("paid", isPaid);
        model.addAttribute("comments", comments);
        model.addAttribute("searchResp", searchUtil.setSearch(searchReq, chapters.getTotalPages()));
        model.addAttribute("lines", lines);
        model.addAttribute("story", story);
        model.addAttribute("chapters", chapters);
        return "ui/story";
    }

    @GetMapping("{storyId}/chapter/{id}")
    public String chapter(@PathVariable Long storyId,
                          @PathVariable Long id,
                          @RequestParam(required = false) Integer sizeCmt,
                          Model model) throws IOException {
        sizeCmt = sizeCmt == null ? 15 : sizeCmt;
        Stories story = storiesService.findById(storyId)
                .orElseThrow(() -> new IllegalArgumentException("Không tồn tại truyện này"));
        if (story.isType()) {
            Account account = getPrincipal();
            if (!account.getRole().equals(ERoles.ROLE_ADMIN) && paidService.findByAccountIdAndStoryId(account.getId(), storyId).isEmpty()) {
                return "redirect:/story/" + storyId;
            }
        }
        Chapter chapter = chapterService.findById(storyId, id).orElseThrow();
        Pageable pageable1 = PageRequest.of(0, sizeCmt, Sort.by("id").descending());
        Page<Comment> comments = commentService.getComment(storyId, pageable1);
        List<Long> ids = chapterService.getIds(storyId, Sort.by("number").ascending());
        List<String> lines = fileUtil.loadText(chapter.getContent(), storyId, ETypeFile.CHAPTER);
        model.addAttribute("comments", comments);
        model.addAttribute("ids", ids);
        model.addAttribute("lines", lines);
        model.addAttribute("chapter", chapter);
        return "ui/chapter";
    }

    @GetMapping("/search")
    public String search(@ModelAttribute SearchReq searchReq,
                         HttpServletRequest req,
                         Model model) {
        searchReq = searchUtil.convertSearchReq(searchReq);
        Pageable pageable = PageRequest.of(searchReq.getPageNumber() - 1, 10);
        Page<Stories> stories = storiesService.findAll(searchReq.getParam(), pageable);
        model.addAttribute("stories", stories);
        model.addAttribute("sizes", storiesService.count(searchReq.getParam()));
        req.getSession().setAttribute("searchResp", searchUtil.setSearch(searchReq, stories.getTotalPages()));
        return "ui/search";
    }


    @PostMapping("/comment")
    public String comment(@ModelAttribute CommentReq commentReq) {
        Comment comment = modelMapper.map(commentReq, Comment.class);
        Account account = accountService.findAccountById(commentReq.getAccountId()).orElseThrow();
        Stories story = storiesService.findById(commentReq.getStoryId()).orElseThrow();
        comment.setAccount(account);
        comment.setStory(story);
        commentService.saveComment(comment);
        return commentReq.getComment();
    }


    private Account getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return accountService.findByEmail(authentication.getName()).orElse(new Account());
    }

    private void setSession(HttpServletRequest req) {
        HttpSession session = req.getSession();
        session.setAttribute("genres", genreService.findAll());
        session.setAttribute("searchResp", new SearchReq());
        session.setAttribute("account", getPrincipal());
    }


}
