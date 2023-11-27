package fa.hust.controllers;

import fa.hust.req.GenreReq;
import fa.hust.req.SearchReq;
import fa.hust.entities.Genre;
import fa.hust.service.GenreService;
import fa.hust.utils.SearchUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;
    private final SearchUtil searchUtil;
    private final ModelMapper modelMapper;

    @GetMapping("/genres")
    public String getGenre(@ModelAttribute SearchReq searchReq,
                           @ModelAttribute GenreReq genreReq,
                           Model model) {
        searchReq = searchUtil.convertSearchReq(searchReq);
        Pageable pageable = PageRequest.of(searchReq.getPageNumber() - 1, searchReq.getPageSize());
        Page<Genre> genres = genreService.findAll(searchReq.getParam(), pageable);
        model.addAttribute("searchResp", searchUtil.setSearch(searchReq, genres.getTotalPages()));
        model.addAttribute("genres", genres);
        return "admin/genre";
    }

    @PostMapping("/genres")
    public String postGenre(@ModelAttribute @Validated GenreReq genreReq, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            Genre genre = modelMapper.map(genreReq, Genre.class);
            genreService.saveGenre(genre);
        }
        model.addAttribute("genreReq",new GenreReq());
        return getGenre(new SearchReq(), new GenreReq(), model);
    }

    @GetMapping("/genres/delete/{id}")
    public String deleteAccounts(@PathVariable Long id) {
        Genre genre = genreService.findGenreById(id).orElseThrow(() -> new UsernameNotFoundException("Genre not found"));
        genreService.deleteGenre(genre);
        return "redirect:/admin/genres";
    }

    @GetMapping("/genre/{id}")
    @ResponseBody
    public GenreReq edit(@PathVariable Long id, @ModelAttribute GenreReq genreReq) {
        Genre genre = genreService.findGenreById(id).orElseThrow(() -> new UsernameNotFoundException("Genre not found"));
        modelMapper.map(genre, genreReq);
        return genreReq;
    }

}
