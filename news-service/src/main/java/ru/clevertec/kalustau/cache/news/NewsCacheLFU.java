package ru.clevertec.kalustau.cache.news;

import ru.clevertec.kalustau.cache.CacheLFU;
import ru.clevertec.kalustau.model.News;

/**
 * Implementation of the LFU algorithm for news caching
 * @author Dzmitry Kalustau
 */
public class NewsCacheLFU extends CacheLFU<News> {

    /**
     * Constructor - new News Cache LFU object
     * @param maxCacheSize - max size
     */
    public NewsCacheLFU(int maxCacheSize) {
        super(maxCacheSize);
    }

}
