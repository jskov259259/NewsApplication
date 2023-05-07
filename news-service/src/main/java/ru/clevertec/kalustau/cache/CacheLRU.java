package ru.clevertec.kalustau.cache;

import ru.clevertec.kalustau.model.BaseEntity;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * LRU cache implementation
 * @author Dzmitry Kalustau
 */
public abstract class CacheLRU<E extends BaseEntity<Long>> extends LinkedHashMap<Long, E> implements Cache<E> {

    private int maxSize;

    /**
     * Constructor - new Cache LRU object
     * @param maxCacheSize - max size
     */
    public CacheLRU(int maxCacheSize) {
        super(maxCacheSize, 0.75f, true);
        this.maxSize = maxCacheSize;
    }

    /**
     * Get entity by id
     * @param id - entity id
     * @return - Entity with given id or null if the entity is not present
     */
    @Override
    public Optional<E> getById(Long id) {
        E entity = get(id);
        if (Objects.isNull(entity)) return Optional.empty();
        put(id, entity);
        return Optional.of(entity);
    }

    /**
     * Save new Entity in collection. If entity present, update.
     * @param entity entity to save
     */
    @Override
    public void save(E entity) {
        update(entity);
    }

    /**
     * Update Entity in collection. If entity is not present, creates new one.
     * @param entity entity to update
     */
    @Override
    public void update(E entity) {
        put(entity.getId(), entity);
    }

    /**
     * Delete Entity from collection
     * @param id entity to delete
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
    protected boolean removeEldestEntry(Map.Entry<Long, E> eldest){
        if (size() <= maxSize) return false;
        return true;
    }

}
