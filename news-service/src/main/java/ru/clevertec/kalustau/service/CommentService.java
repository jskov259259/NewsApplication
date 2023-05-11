package ru.clevertec.kalustau.service;

import ru.clevertec.kalustau.model.Comment;

import java.util.List;

public interface CommentService {

    /**
     * Returns a list of comments with pagination and sorting.
     * @param pageNo parameter for a specific page
     * @param pageSize parameter for page size
     * @param sortBy parameter for sorting the result
     * @return a list of comments.
     */
    List<Comment> findAll(String search, Integer pageNo, Integer pageSize, String sortBy);

    /**
     * Return comment by id.
     * @param id parameter for the id of a certain comment
     * @return comment with given id
     */
    Comment findById(Long id);

    /**
     * Returns all comments by specifying news id.
     * @param newsId parameter for the id of a certain news
     * @param pageNo parameter for a specific page
     * @param pageSize parameter for page size
     * @param sortBy parameter for sorting the result
     * @return A JSON representation of the comments of the given news
     */
    List<Comment> findAllByNewsId(Long newsId, Integer pageNo, Integer pageSize, String sortBy);

    /**
     * Create comment.
     * @param newsId parameter for the id of a certain news
     * @param comment object to save a comment to a specific news
     * @return created comment
     */
    Comment save(Long newsId, Comment comment, String token);

    /**
     * Update news by specifying id.
     * @param comment object to update
     * @return updated news
     */
    Comment update(Long id, Comment comment, String token);

    /**
     * Delete comment by specifying id.
     * @param id id to delete
     */
    void deleteById(Long id, String token);

}
