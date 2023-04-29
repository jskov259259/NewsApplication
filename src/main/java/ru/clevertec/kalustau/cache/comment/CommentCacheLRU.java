package ru.clevertec.kalustau.cache.comment;

import ru.clevertec.kalustau.cache.Cache;
import ru.clevertec.kalustau.model.Comment;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class CommentCacheLRU extends LinkedHashMap<Long, Comment> implements Cache<Comment> {

    /** Field max collection size */
    private int maxSize;

    /**
     * Constructor - new User Dao Cache LRU object
     * @param maxCacheSize - max size
     */
    public CommentCacheLRU(int maxCacheSize) {
        super(maxCacheSize, 0.75f, true);
        this.maxSize = maxCacheSize;
    }

    /**
     * Get user by id
     * @param id - user id
     * @return - User with given id or null if the user is not present
     */
    @Override
    public Optional<Comment> getById(Long id) {
        Comment comment = get(id);
        if (Objects.isNull(comment)) return Optional.empty();
        put(id, comment);
        return Optional.of(comment);
    }

    /**
     * Save new User in collection. If comment present, update.
     * @param comment comment to save
     */
    @Override
    public void save(Comment comment) {
        update(comment);
    }

    /**
     * Update User in collection. If comment is not present, creates new one.
     * @param comment comment to update
     */
    @Override
    public void update(Comment comment) {
        put(comment.getId(), comment);
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
    protected boolean removeEldestEntry(Map.Entry<Long, Comment> eldest){
        if (size() <= maxSize) return false;
        return true;
    }

}
