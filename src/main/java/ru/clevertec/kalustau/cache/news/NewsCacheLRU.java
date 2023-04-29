package ru.clevertec.kalustau.cache.news;

import ru.clevertec.kalustau.cache.Cache;
import ru.clevertec.kalustau.model.News;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class NewsCacheLRU extends LinkedHashMap<Long, News> implements Cache<News> {

    /** Field max collection size */
    private int maxSize;

    /**
     * Constructor - new User Dao Cache LRU object
     * @param maxCacheSize - max size
     */
    public NewsCacheLRU(int maxCacheSize) {
        super(maxCacheSize, 0.75f, true);
        this.maxSize = maxCacheSize;
    }

    /**
     * Get user by id
     * @param id - user id
     * @return - User with given id or null if the user is not present
     */
    @Override
    public Optional<News> getById(Long id) {
        News news = get(id);
        if (Objects.isNull(news)) return Optional.empty();
        put(id, news);
        return Optional.of(news);
    }

    /**
     * Save new User in collection. If news present, update.
     * @param news news to save
     */
    @Override
    public void save(News news) {
        update(news);
    }

    /**
     * Update User in collection. If news is not present, creates new one.
     * @param news news to update
     */
    @Override
    public void update(News news) {
        put(news.getId(), news);
    }

    /**
     * Delete User from collection
     * @param id news to delete
     */
    @Override
    public void delete(Long id) {
        remove(id);
    }

    /**
     * Override method from basic class to control max size collection
     * @param eldest - eldest entry to remove
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry<Long, News> eldest){
        if (size() <= maxSize) return false;
        return true;
    }

}
