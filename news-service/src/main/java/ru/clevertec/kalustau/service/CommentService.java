package ru.clevertec.kalustau.service;

import ru.clevertec.kalustau.dto.CommentDto;

import java.util.List;

public interface CommentService {

    /**
     * Returns a list of comments with pagination and sorting.
     * @param pageNo parameter for a specific page
     * @param pageSize parameter for page size
     * @param sortBy parameter for sorting the result
     * @return a list of comments.
     */
    List<CommentDto> findAll(String search, Integer pageNo, Integer pageSize, String sortBy);

    /**
     * Return comment by id.
     * @param id parameter for the id of a certain comment
     * @return comment with given id
     */
    CommentDto findById(Long id);

    /**
     * Returns all comments by specifying news id.
     * @param newsId parameter for the id of a certain news
     * @param pageNo parameter for a specific page
     * @param pageSize parameter for page size
     * @param sortBy parameter for sorting the result
     * @return A JSON representation of the comments of the given news
     */
    List<CommentDto> findAllByNewsId(Long newsId, Integer pageNo, Integer pageSize, String sortBy);

    /**
     * Create comment.
     * @param newsId parameter for the id of a certain news
     * @param commentDto object to save a comment to a specific news
     * @return created comment
     */
    CommentDto save(Long newsId, CommentDto commentDto);

    /**
     * Update news by specifying id.
     * @param commentDto object to update
     * @return updated news
     */
    CommentDto update(Long id, CommentDto commentDto);

    /**
     * Delete comment by specifying id.
     * @param id id to delete
     */
    void deleteById(Long id);

}
