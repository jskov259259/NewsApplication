package ru.clevertec.kalustau.controller.config;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

}
