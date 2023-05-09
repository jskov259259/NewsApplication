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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.kalustau.annotation.ControllerLog;
import ru.clevertec.kalustau.dto.NewsDtoRequest;
import ru.clevertec.kalustau.dto.Proto;
import ru.clevertec.kalustau.model.News;
import ru.clevertec.kalustau.service.NewsService;

import java.util.List;

import static ru.clevertec.kalustau.controller.config.Constants.DEFAULT_PAGE_NO;
import static ru.clevertec.kalustau.controller.config.Constants.DEFAULT_PAGE_SIZE;
import static ru.clevertec.kalustau.controller.config.Constants.DEFAULT_SORT_BY;
import static ru.clevertec.kalustau.controller.config.Constants.NEWS_URL;
import static ru.clevertec.kalustau.util.JsonProtobufUtility.toJson;

/**
 * REST controller for News operations.
 * @author Dzmitry Kalustau
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(NEWS_URL)
@ControllerLog
@Tag(name = "News", description = "News management APIs")
public class NewsController {

    private final NewsService newsService;

    /**
     * API Point for returning news page.
     * @param search parameter for filtered search, example: http://localhost:8080/news?search=title:text,id>5
     * @param pageNo parameter for a specific page
     * @param pageSize parameter for page size
     * @param sortBy parameter for sorting the result
     * @return A JSON representation of the news
     */
    @Operation(summary = "Retrieve all news", description = "Get list of all news")
    @ApiResponses({@ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = News.class),
            mediaType = "application/json") })})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> findAll(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
            @RequestParam(defaultValue = DEFAULT_SORT_BY) String sortBy) {
        List<Proto.NewsDtoResponse> news = newsService.findAll(search, pageNo, pageSize, sortBy);
        return new ResponseEntity<>(toJson(news), HttpStatus.OK);
    }

    /**
     * API Point for returning news by id.
     * @param id parameter for the id of a certain news
     * @return A JSON representation of the news
     */
    @Operation(summary = "Retrieve a news by Id", description = "Get a News object by specifying its id")
    @ApiResponses({@ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = News.class),
            mediaType = "application/json") }),
            @ApiResponse(description = "Such news not found", responseCode = "404", content = { @Content(schema = @Schema()) })})
    @GetMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> findById(@PathVariable Long id) {
        Proto.NewsDtoResponse news = newsService.findById(id);
        return new ResponseEntity<>(toJson(news), HttpStatus.OK);
    }

    /**
     * API Point for saving news.
     * @param newsDtoRequest object to save
     * @return A JSON representation of the created news
     */
    @Operation(summary = "Post new news", description = "Save new news in DB")
    @ApiResponses({@ApiResponse(responseCode = "201", content = { @Content(schema = @Schema(implementation = News.class),
            mediaType = "application/json") })})
    @PostMapping(consumes =MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> create(
            @RequestBody @Valid NewsDtoRequest newsDtoRequest,
            @RequestHeader("Authorization") String token) {
        Proto.NewsDtoResponse createdNewsDto = newsService.save(newsDtoRequest, token);
        return new ResponseEntity<>(toJson(createdNewsDto), HttpStatus.CREATED);
    }

    /**
     * API Point for updating news by id.
     * @param newsDtoRequest object to update
     * @param id id of the news to be updated
     * @return A JSON representation of the updated news
     */
    @Operation(summary = "Update news", description = "Update existed news by id")
    @ApiResponses({@ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = News.class),
            mediaType = "application/json") }),
            @ApiResponse(description = "Such news not found", responseCode = "404", content = { @Content(schema = @Schema()) })})
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> update(
            @PathVariable Long id,
            @RequestBody @Valid NewsDtoRequest newsDtoRequest,
            @RequestHeader("Authorization") String token) {
        Proto.NewsDtoResponse news = newsService.update(id, newsDtoRequest, token);
        return new ResponseEntity<>(toJson(news), HttpStatus.OK);
    }

    /**
     * API Point for deleting news.
     * @param id id of the news to be deleted
     * @return response about the successful deletion of the news
     */
    @Operation(summary = "Delete news", description = "Delete news by id")
    @ApiResponses({@ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json") })})
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        newsService.deleteById(id, token);
        return new ResponseEntity(HttpStatus.OK);
    }

}
