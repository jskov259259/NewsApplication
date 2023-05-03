package ru.clevertec.kalustau.cache.news;

import ru.clevertec.kalustau.cache.CacheLRU;
import ru.clevertec.kalustau.model.News;

public class NewsCacheLRU extends CacheLRU<News> {

    /**
     * Constructor - new News Cache LRU object
     * @param maxCacheSize - max size
     */
    public NewsCacheLRU(int maxCacheSize) {
        super(maxCacheSize);
    }

}
