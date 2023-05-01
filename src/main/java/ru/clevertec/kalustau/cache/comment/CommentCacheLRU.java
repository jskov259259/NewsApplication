package ru.clevertec.kalustau.cache.comment;

import ru.clevertec.kalustau.cache.CacheLFU;
import ru.clevertec.kalustau.model.Comment;

public class CommentCacheLRU extends CacheLFU<Comment> {

    /**
     * Constructor - new Comment Cache LFU object
     * @param maxCacheSize - max size
     */
    public CommentCacheLRU(int maxCacheSize) {
        super(maxCacheSize);
    }

}
