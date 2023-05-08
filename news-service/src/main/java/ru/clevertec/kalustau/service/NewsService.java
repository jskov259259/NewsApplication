package ru.clevertec.kalustau.service;

import java.util.List;
import ru.clevertec.kalustau.dto.Proto.NewsDto;

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
    List<NewsDto> findAll(String search, Integer pageNo, Integer pageSize, String sortBy);

    /**
     * Return news by id.
     * @param id parameter for the id of a certain news
     * @return news with given id
     */
    NewsDto findById(Long id);

    /**
     * Create news.
     * @param newsDto object to save
     * @return created news
     */
    NewsDto save(NewsDto newsDto);

    /**
     * Update news by specifying id.
     * @param newsDto object to save
     * @return updated news
     */
    NewsDto update(Long id, NewsDto newsDto);

    /**
     * Delete news by specifying id.
     * @param id id to delete
     */
    void deleteById(Long id);
}
