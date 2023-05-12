package ru.clevertec.kalustau.cache.news;

import ru.clevertec.kalustau.cache.CacheLRU;
import ru.clevertec.kalustau.model.News;

/**
 * Implementation of the LRU algorithm for news caching
 * @author Dzmitry Kalustau
 */
public class NewsCacheLRU extends CacheLRU<News> {

    /**
     * Constructor - new News Cache LRU object
     * @param maxCacheSize - max size
     */
    public NewsCacheLRU(int maxCacheSize) {
        super(maxCacheSize);
    }

}
