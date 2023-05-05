package ru.clevertec.kalustau.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import ru.clevertec.kalustau.annotation.ControllerLog;
import ru.clevertec.kalustau.model.News;
import ru.clevertec.kalustau.service.NewsService;
import ru.clevertec.kalustau.dto.Proto.NewsDto;

import java.util.List;

import static ru.clevertec.kalustau.controller.config.Constants.DEFAULT_PAGE_NO;
import static ru.clevertec.kalustau.controller.config.Constants.DEFAULT_PAGE_SIZE;
import static ru.clevertec.kalustau.controller.config.Constants.DEFAULT_SORT_BY;
import static ru.clevertec.kalustau.controller.config.Constants.NEWS_URL;
import static ru.clevertec.kalustau.util.JsonProtobufUtility.toJson;

@RequiredArgsConstructor
@RestController
@RequestMapping(NEWS_URL)
@ControllerLog
@Tag(name = "News", description = "News management APIs")
public class NewsController {

    private final NewsService newsService;

    @Operation(summary = "Retrieve all news", description = "Get list of all news")
    @ApiResponses({@ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = News.class),
            mediaType = "application/json") })})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> findAll(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
            @RequestParam(defaultValue = DEFAULT_SORT_BY) String sortBy) {
        List<NewsDto> news = newsService.findAll(search, pageNo, pageSize, sortBy);
        return new ResponseEntity<>(toJson(news), HttpStatus.OK);
    }

    @Operation(summary = "Retrieve a news by Id", description = "Get a News object by specifying its id")
    @ApiResponses({@ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = News.class),
            mediaType = "application/json") }),
            @ApiResponse(description = "Such news not found", responseCode = "404", content = { @Content(schema = @Schema()) })})
    @GetMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> findById(@PathVariable Long id) {
        NewsDto news = newsService.findById(id);
        return new ResponseEntity<>(toJson(news), HttpStatus.OK);
    }

    @Operation(summary = "Post new news", description = "Save new news in DB")
    @ApiResponses({@ApiResponse(responseCode = "201", content = { @Content(schema = @Schema(implementation = News.class),
            mediaType = "application/json") })})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> create(@RequestBody @Valid NewsDto newsDto) {
        NewsDto createdNewsDto = newsService.save(newsDto);
        return new ResponseEntity<>(toJson(createdNewsDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Update news", description = "Update existed news by id")
    @ApiResponses({@ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = News.class),
            mediaType = "application/json") }),
            @ApiResponse(description = "Such news not found", responseCode = "404", content = { @Content(schema = @Schema()) })})
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> update(@PathVariable Long id,
                                                     @RequestBody @Valid NewsDto newsDto) {
        NewsDto news = newsService.update(id, newsDto);
        return new ResponseEntity<>(toJson(news), HttpStatus.OK);
    }

    @Operation(summary = "Delete news", description = "Delete news by id")
    @ApiResponses({@ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json") })})
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        newsService.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

}
