package ru.clevertec.kalustau.cache;

import ru.clevertec.kalustau.model.BaseEntity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 * LFU cache implementation
 * @author Dzmitry Kalustau
 */
public abstract class CacheLFU<E extends BaseEntity<Long>> implements Cache<E> {

    private Map<Long, E> valueMap = new HashMap<>();
    private Map<Long, Integer> countMap = new HashMap<>();
    private TreeMap<Integer, List<E>> frequencyMap = new TreeMap<>();
    private final int maxSize;

    /**
     * Constructor - new Cache LFU object
     * @param maxCacheSize - max size
     */
    public CacheLFU(int maxCacheSize) {
        this.maxSize = maxCacheSize;
    }

    /**
     * Get entity by id
     * @param id - entity id
     * @return - Entity with given id or null if the entity is not present
     */
    @Override
    public Optional<E> getById(Long id) {
        if (!valueMap.containsKey(id) || maxSize == 0) return Optional.empty();

        int frequency = countMap.get(id);
        E value = frequencyMap.get(frequency).stream().filter(entity -> entity.getId() == id)
                .findFirst().get();
        frequencyMap.get(frequency).remove(value);
        if (frequencyMap.get(frequency).size() == 0)
            frequencyMap.remove(frequency);

        frequencyMap.computeIfAbsent(frequency + 1, v -> new LinkedList<>()).add(value);
        countMap.put(id, frequency + 1);
        return Optional.of(valueMap.get(id));
    }

    /**
     * Save new Entity in collections. If entity present, update.
     * @param entity entity to save
     */
    @Override
    public void save(E entity) {
        if (valueMap.containsKey(entity.getId()) == false && maxSize > 0) {
            if (valueMap.size() == maxSize) {
                Integer lowestCount = frequencyMap.firstKey();
                E entityToDelete = frequencyMap.get(lowestCount).remove(0);

                if (frequencyMap.get(lowestCount).size() == 0)
                    frequencyMap.remove(lowestCount);

                valueMap.remove(entityToDelete.getId());
                countMap.remove(entityToDelete.getId());
            }

            valueMap.put(entity.getId(), entity);
            countMap.put(entity.getId(), 1);
            frequencyMap.computeIfAbsent(1, v -> new LinkedList<>()).add(entity);
        } else if (maxSize > 0) {
            valueMap.put(entity.getId(), entity);

            Integer frequency = countMap.get(entity.getId());
            E value = frequencyMap.get(frequency).stream().filter(e -> e.getId() == entity.getId())
                    .findFirst().get();
            frequencyMap.get(frequency).remove(value);

            if (frequencyMap.get(frequency).size() == 0)
                frequencyMap.remove(frequency);

            frequencyMap.computeIfAbsent(frequency + 1, v -> new LinkedList<>()).add(value);
            countMap.put(entity.getId(), frequency + 1);
        }
    }

    /**
     * Update Entity in collections. If entity is not present, creates new one.
     * @param e entity to update
     */
    @Override
    public void update(E e) {
        save(e);
    }

    /**
     * Delete Entity from all collections
     * @param id id to delete
     */
    @Override
    public void delete(Long id) {
        E value = valueMap.get(id);
        valueMap.remove(id);
        int frequency = countMap.get(id);
        countMap.remove(id);
        frequencyMap.get(frequency).remove(value);
    }

    /**
     * Clear cache
     */
    @Override
    public void clear() {
        valueMap.clear();
        countMap.clear();
        frequencyMap.clear();
    }

    /**
     * Returns the number of elements in this cache
     */
    public int size() {
        return valueMap.size();
    }
}
