package ru.clevertec.kalustau.service;

import java.util.List;

import ru.clevertec.kalustau.dto.NewsDtoRequest;
import ru.clevertec.kalustau.dto.Proto;

/**
 * A service interface that provides CRUD operations for news.
 * @author Dzmitry Kalustau
 */
public interface NewsService {

    /**
     * Returns a list of news with pagination and sorting.
     * @param pageNo parameter for a specific page
     * @param pageSize parameter for page size
     * @param sortBy parameter for sorting the result
     * @return a list of news.
     */
    List<Proto.NewsDtoResponse> findAll(String search, Integer pageNo, Integer pageSize, String sortBy);

    /**
     * Return news by id.
     * @param id parameter for the id of a certain news
     * @return news with given id
     */
    Proto.NewsDtoResponse findById(Long id);

    /**
     * Create news.
     * @param newsDto object to save
     * @return created news
     */
    Proto.NewsDtoResponse save(NewsDtoRequest newsDto, String token);

    /**
     * Update news by specifying id.
     * @param newsDto object to save
     * @return updated news
     */
    Proto.NewsDtoResponse update(Long id, NewsDtoRequest newsDto, String token);

    /**
     * Delete news by specifying id.
     * @param id id to delete
     */
    void deleteById(Long id, String token);
}
