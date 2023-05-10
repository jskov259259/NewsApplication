package ru.clevertec.kalustau.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.kalustau.annotation.ControllerLog;
import ru.clevertec.kalustau.dto.CommentDtoRequest;
import ru.clevertec.kalustau.dto.Proto;
import ru.clevertec.kalustau.model.Comment;
import ru.clevertec.kalustau.service.CommentService;

import java.util.List;

import static ru.clevertec.kalustau.controller.config.Constants.COMMENTS_URL;
import static ru.clevertec.kalustau.controller.config.Constants.DEFAULT_PAGE_NO;
import static ru.clevertec.kalustau.controller.config.Constants.DEFAULT_PAGE_SIZE;
import static ru.clevertec.kalustau.controller.config.Constants.DEFAULT_SORT_BY;
import static ru.clevertec.kalustau.controller.config.Constants.NEWS_URL;
import static ru.clevertec.kalustau.util.JsonProtobufUtility.toJson;

/**
 * REST controller for Comment operations.
 * @author Dzmitry Kalustau
 */
@RequiredArgsConstructor
@RestController
@ControllerLog
@Tag(name = "Comment", description = "Comment management APIs")
public class CommentController {

    private final CommentService commentsService;

    /**
     * API Point for returning all comments.
     * @param search parameter for filtered search, example: http://localhost:8080/comments?search=userName:doe,id>5
     * @param pageNo parameter for a specific page
     * @param pageSize parameter for page size
     * @param sortBy parameter for sorting the result
     * @return A JSON representation of the comments
     */
    @Operation(summary = "Retrieve all Comments", description = "Get list of all comments for all news")
    @ApiResponses({@ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Comment.class),
                    mediaType = "application/json") })})
    @GetMapping(value=COMMENTS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> findAll(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
            @RequestParam(defaultValue = DEFAULT_SORT_BY) String sortBy) {
        List<Proto.CommentDtoResponse> comments = commentsService.findAll(search, pageNo, pageSize, sortBy);
        return new ResponseEntity<>(toJson(comments), HttpStatus.OK);
    }

    /**
     * API Point for returning comment by id.
     * @param id parameter for the id of a certain comment
     * @return A JSON representation of the comment
     */
    @Operation(summary = "Retrieve a comment by Id", description = "Get a Tutorial object by specifying its id")
    @ApiResponses({@ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Comment.class),
            mediaType = "application/json") }),
            @ApiResponse(description = "Such comment not found", responseCode = "404", content = { @Content(schema = @Schema()) })})
    @GetMapping(value=COMMENTS_URL + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> findById(@PathVariable Long id) {
        Proto.CommentDtoResponse comment = commentsService.findById(id);
        return new ResponseEntity<>(toJson(comment), HttpStatus.OK);
    }

    /**
     * API Point for returning all comments by specifying news id.
     * @param newsId parameter for the id of a certain news
     * @param pageNo parameter for a specific page
     * @param pageSize parameter for page size
     * @param sortBy parameter for sorting the result
     * @return A JSON representation of the comments of the given news
     */
    @Operation(summary = "Retrieve all Comments by news ID", description = "Get list of all comments by news id")
    @ApiResponses({@ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Comment.class),
            mediaType = "application/json") }),
            @ApiResponse(description = "Such news not found", responseCode = "404", content = { @Content(schema = @Schema()) })})
    @GetMapping(value = NEWS_URL + "/{newsId}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> findAllCommentsByNewsId(
            @PathVariable(value = "newsId") Long newsId,
            @RequestParam(defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
            @RequestParam(defaultValue = DEFAULT_SORT_BY) String sortBy) {

        List<Proto.CommentDtoResponse> comments = commentsService.findAllByNewsId(newsId, pageNo, pageSize, sortBy);
        return new ResponseEntity<>(toJson(comments), HttpStatus.OK);
    }

    /**
     * API Point for saving comment.
     * @param newsId parameter for the id of a certain news
     * @param commentDtoRequest object to save a comment to a specific news
     * @return A JSON representation of the created comment
     */
    @Operation(summary = "Post new comment", description = "Save new comment for news")
    @ApiResponses({@ApiResponse(responseCode = "201", content = { @Content(schema = @Schema(implementation = Comment.class),
            mediaType = "application/json") })})
    @PostMapping(value = NEWS_URL + "/{newsId}/comments", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> create(
            @PathVariable(value = "newsId") Long newsId,
            @RequestBody @Valid CommentDtoRequest commentDtoRequest,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Proto.CommentDtoResponse createdComment = commentsService.save(newsId, commentDtoRequest, token);
        return new ResponseEntity<>(toJson(createdComment), HttpStatus.CREATED);
    }

    /**
     * API Point for updating comment by id.
     * @param commentDtoRequest object to update
     * @param id id of the comment to be updated
     * @return A JSON representation of the updated comment
     */
    @Operation(summary = "Update comment", description = "Update existed comment by id")
    @ApiResponses({@ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Comment.class),
            mediaType = "application/json") }),
            @ApiResponse(description = "Such comment not found", responseCode = "404", content = { @Content(schema = @Schema()) })})
    @PutMapping(value = COMMENTS_URL + "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> update(
            @PathVariable Long id,
            @RequestBody @Valid CommentDtoRequest commentDtoRequest,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Proto.CommentDtoResponse comment = commentsService.update(id, commentDtoRequest, token);
        return new ResponseEntity<>(toJson(comment), HttpStatus.OK);
    }

    /**
     * API Point for deleting comment.
     * @param id id of the comment to be deleted
     * @return response about the successful deletion of the comment
     */
    @Operation(summary = "Delete comment", description = "Delete comment by id")
    @ApiResponses({@ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json") })})
    @DeleteMapping(value = COMMENTS_URL + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(
            @PathVariable Long id,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        commentsService.deleteById(id, token);
        return new ResponseEntity(HttpStatus.OK);
    }
}
