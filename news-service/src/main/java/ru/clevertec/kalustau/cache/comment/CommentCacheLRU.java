package ru.clevertec.kalustau.cache.comment;

import ru.clevertec.kalustau.cache.CacheLRU;
import ru.clevertec.kalustau.model.Comment;

/**
 * Implementation of the LRU algorithm for comment caching
 * @author Dzmitry Kalustau
 */
public class CommentCacheLRU extends CacheLRU<Comment> {

    /**
     * Constructor - new Comment Cache LFU object
     * @param maxCacheSize - max size
     */
    public CommentCacheLRU(int maxCacheSize) {
        super(maxCacheSize);
    }

}
