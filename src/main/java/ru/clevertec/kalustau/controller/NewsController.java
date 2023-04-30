package ru.clevertec.kalustau.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.kalustau.aop.annotation.Log;
import ru.clevertec.kalustau.dto.NewsDto;
import ru.clevertec.kalustau.service.NewsService;

import java.util.List;

import static ru.clevertec.kalustau.controller.config.Constants.DEFAULT_PAGE_NO;
import static ru.clevertec.kalustau.controller.config.Constants.DEFAULT_PAGE_SIZE;
import static ru.clevertec.kalustau.controller.config.Constants.DEFAULT_SORT_BY;
import static ru.clevertec.kalustau.controller.config.Constants.NEWS_URL;

@RequiredArgsConstructor
@RestController
@RequestMapping(NEWS_URL)
@Log
@Tag(name = "News", description = "News management APIs")
public class NewsController {

    private final NewsService newsService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<NewsDto>> findAll(
            @RequestParam(defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
            @RequestParam(defaultValue = DEFAULT_SORT_BY) String sortBy) {
        List<NewsDto> news = newsService.findAll(pageNo, pageSize, sortBy);
        return new ResponseEntity<>(news, HttpStatus.OK);
    }

    @GetMapping(value="/{id}", produces = "application/json")
    public ResponseEntity<NewsDto> findById(@PathVariable Long id) {
        NewsDto news = newsService.findById(id);
        return new ResponseEntity<>(news, HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<NewsDto> create(@RequestBody @Valid NewsDto newsDto) {
        NewsDto createdNewsDto = newsService.save(newsDto);
        return new ResponseEntity<>(createdNewsDto, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<NewsDto> update(@PathVariable Long id,
                                                     @RequestBody @Valid NewsDto newsDto) {
        newsDto.setId(id);
        NewsDto news = newsService.update(newsDto);
        return new ResponseEntity<>(news, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = {"application/json"})
    public ResponseEntity<?> delete(@PathVariable Long id) {
        newsService.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

}
