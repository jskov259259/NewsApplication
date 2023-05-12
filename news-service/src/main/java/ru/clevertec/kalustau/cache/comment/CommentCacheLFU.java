package ru.clevertec.kalustau.cache.comment;

import ru.clevertec.kalustau.cache.CacheLFU;
import ru.clevertec.kalustau.model.Comment;

/**
 * Implementation of the LFU algorithm for comment caching
 * @author Dzmitry Kalustau
 */
public class CommentCacheLFU extends CacheLFU<Comment> {

    /**
     * Constructor - new Comment Cache LFU object
     * @param maxCacheSize - max size
     */
    public CommentCacheLFU(int maxCacheSize) {
        super(maxCacheSize);
    }

}
