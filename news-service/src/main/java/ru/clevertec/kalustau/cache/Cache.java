package ru.clevertec.kalustau.cache;

import ru.clevertec.kalustau.model.BaseEntity;

import java.util.Optional;

/**
 * Interface providing methods for caches
 * @author Dzmitry Kalustau
 */
public interface Cache<E extends BaseEntity<Long>> {

    /**
     * Returns the entity as an Optional object
     * @param id id of requested entity
     * @return the entity by given id
     */
    Optional<E> getById(Long id);

    /**
     * Save the entity in cache
     * @param e entity to save
     */
    void save(E e);

    /**
     * Update the entity in cache
     * @param e entity to update
     */
    void update(E e);

    /**
     * Delete the entity in cache by specifying id
     * @param id id of the entity to delete
     */
    void delete(Long id);

    /**
     * Delete all entities in cache
     */
    void clear();
}
