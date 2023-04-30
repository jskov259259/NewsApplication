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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.kalustau.aop.annotation.Log;
import ru.clevertec.kalustau.dto.CommentDto;
import ru.clevertec.kalustau.service.CommentService;

import java.util.List;

import static ru.clevertec.kalustau.controller.config.Constants.COMMENTS_URL;
import static ru.clevertec.kalustau.controller.config.Constants.DEFAULT_PAGE_NO;
import static ru.clevertec.kalustau.controller.config.Constants.DEFAULT_PAGE_SIZE;
import static ru.clevertec.kalustau.controller.config.Constants.DEFAULT_SORT_BY;
import static ru.clevertec.kalustau.controller.config.Constants.NEWS_URL;

@RequiredArgsConstructor
@RestController
@Log
@Tag(name = "Comment", description = "Comment management APIs")
public class CommentController {

    private final CommentService commentsService;

    @GetMapping(value=COMMENTS_URL, produces = "application/json")
    public ResponseEntity<List<CommentDto>> findAll(
            @RequestParam(defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
            @RequestParam(defaultValue = DEFAULT_SORT_BY) String sortBy) {

        List<CommentDto> comments = commentsService.findAll(pageNo, pageSize, sortBy);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @GetMapping(value=COMMENTS_URL + "/{id}", produces = "application/json")
    public ResponseEntity<CommentDto> findById(@PathVariable Long id) {
        CommentDto comment = commentsService.findById(id);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @GetMapping(value = NEWS_URL + "/{newsId}/comments")
    public ResponseEntity<List<CommentDto>> findAllCommentsByNewsId(
            @PathVariable(value = "newsId") Long newsId,
            @RequestParam(defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
            @RequestParam(defaultValue = DEFAULT_SORT_BY) String sortBy) {

        List<CommentDto> comments = commentsService.findAllByNewsId(newsId, pageNo, pageSize, sortBy);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PostMapping(value = NEWS_URL + "/{newsId}/comments", consumes = "application/json", produces = "application/json")
    public ResponseEntity<CommentDto> create(@PathVariable(value = "newsId") Long newsId,
                                             @RequestBody @Valid CommentDto commentDto) {
        CommentDto createdComment = commentsService.save(newsId, commentDto);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    @PutMapping(value = COMMENTS_URL + "/{id}", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<CommentDto> update(@PathVariable Long id, @RequestBody @Valid CommentDto commentDto) {
        commentDto.setId(id);
        CommentDto comment = commentsService.update(commentDto);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @DeleteMapping(value = COMMENTS_URL + "/{id}", produces = {"application/json"})
    public ResponseEntity<?> delete(@PathVariable Long id) {
        commentsService.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
