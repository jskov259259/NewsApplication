package ru.clevertec.kalustau.cache.news;

import ru.clevertec.kalustau.cache.CacheLFU;
import ru.clevertec.kalustau.model.News;

public class NewsCacheLFU extends CacheLFU<News> {

    /**
     * Constructor - new News Cache LFU object
     * @param maxCacheSize - max size
     */
    public NewsCacheLFU(int maxCacheSize) {
        super(maxCacheSize);
    }

}
