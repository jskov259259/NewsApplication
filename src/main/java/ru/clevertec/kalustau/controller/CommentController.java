package ru.clevertec.kalustau.controller;

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
import ru.clevertec.kalustau.dto.CommentDto;
import ru.clevertec.kalustau.service.CommentService;

import java.util.List;

import static ru.clevertec.kalustau.controller.config.Constants.COMMENTS_URL;
import static ru.clevertec.kalustau.controller.config.Constants.DEFAULT_PAGE_NO;
import static ru.clevertec.kalustau.controller.config.Constants.DEFAULT_PAGE_SIZE;
import static ru.clevertec.kalustau.controller.config.Constants.DEFAULT_SORT_BY;

@RequiredArgsConstructor
@RestController
@RequestMapping(COMMENTS_URL)
public class CommentController {

    private final CommentService commentsService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<CommentDto>> findAll(
            @RequestParam(defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
            @RequestParam(defaultValue = DEFAULT_SORT_BY) String sortBy) {
        List<CommentDto> comments = commentsService.findAll(pageNo, pageSize, sortBy);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @GetMapping(value="/{id}", produces = "application/json")
    public ResponseEntity<CommentDto> findById(@PathVariable Long id) {
        CommentDto comment = commentsService.findById(id);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<CommentDto> create(@RequestBody @Valid CommentDto commentDto) {
        CommentDto createdComment = commentsService.save(commentDto);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<CommentDto> update(@PathVariable Long id,
                                          @RequestBody @Valid CommentDto commentDto) {
        commentDto.setId(id);
        CommentDto comment = commentsService.update(commentDto);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = {"application/json"})
    public ResponseEntity<?> delete(@PathVariable Long id) {
        commentsService.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
