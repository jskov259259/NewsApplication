package ru.clevertec.kalustau.cache.comment;

import ru.clevertec.kalustau.cache.Cache;
import ru.clevertec.kalustau.model.Comment;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class CommentCacheLFU implements Cache<Comment> {

    /** Map with user IDs and users */
    private Map<Long, Comment> valueMap = new HashMap<>();
    /** Map with user IDs and count of usage */
    private Map<Long, Integer> countMap = new HashMap<>();
    /** Map with count of usage and list with users */
    private TreeMap<Integer, List<Comment>> frequencyMap = new TreeMap<>();
    /** Field max collection size */
    private final int maxSize;

    /**
     * Constructor - new User Dao Cache LFU object
     * @param maxCacheSize - max size
     */
    public CommentCacheLFU(int maxCacheSize) {
        this.maxSize = maxCacheSize;
    }

    /**
     * Get user by id
     * @param id - user id
     * @return - User with given id or null if the user is not present
     */
    @Override
    public Optional<Comment> getById(Long id) {
        if (!valueMap.containsKey(id) || maxSize == 0) return Optional.empty();

        int frequency = countMap.get(id);
        Comment commentValue = frequencyMap.get(frequency).stream().filter(comment -> comment.getId() == id)
                .findFirst().get();
        frequencyMap.get(frequency).remove(commentValue);
        if (frequencyMap.get(frequency).size() == 0)
            frequencyMap.remove(frequency);

        frequencyMap.computeIfAbsent(frequency + 1, v -> new LinkedList<>()).add(commentValue);
        countMap.put(id, frequency + 1);
        return Optional.of(valueMap.get(id));
    }

    /**
     * Save new User in collections. If user present, update.
     * @param comment user to save
     */
    @Override
    public void save(Comment comment) {
        if (valueMap.containsKey(comment.getId()) == false && maxSize > 0) {
            if (valueMap.size() == maxSize) {
                Integer lowestCount = frequencyMap.firstKey();
                Comment commentToDelete = frequencyMap.get(lowestCount).remove(0);

                if (frequencyMap.get(lowestCount).size() == 0)
                    frequencyMap.remove(lowestCount);

                valueMap.remove(commentToDelete.getId());
                countMap.remove(commentToDelete.getId());
            }

            valueMap.put(comment.getId(), comment);
            countMap.put(comment.getId(), 1);
            frequencyMap.computeIfAbsent(1, v -> new LinkedList<>()).add(comment);
        } else if (maxSize > 0) {
            valueMap.put(comment.getId(), comment);

            Integer frequency = countMap.get(comment.getId());
            Comment commentValue = frequencyMap.get(frequency).stream().filter(c -> c.getId() == comment.getId())
                    .findFirst().get();
            frequencyMap.get(frequency).remove(commentValue);

            if (frequencyMap.get(frequency).size() == 0)
                frequencyMap.remove(frequency);

            frequencyMap.computeIfAbsent(frequency + 1, v -> new LinkedList<>()).add(commentValue);
            countMap.put(comment.getId(), frequency + 1);
        }
    }

    /**
     * Update User in collections. If user is not present, creates new one.
     * @param comment user to update
     */
    @Override
    public void update(Comment comment) {
        save(comment);
    }

    /**
     * Delete User from all collections
     * @param id id to delete
     */
    @Override
    public void delete(Long id) {
        Comment comment = valueMap.get(id);
        valueMap.remove(id);
        int frequency = countMap.get(id);
        countMap.remove(id);
        frequencyMap.get(frequency).remove(comment);
    }

    public int size() {
        return valueMap.size();
    }

    public void display() {
        System.out.println(valueMap.keySet().stream()
                .map(key -> key + "=" + valueMap.get(key))
                .collect(Collectors.joining(", ", "{", "}")));
    }

}
