package ru.clevertec.kalustau.cache.news;

import ru.clevertec.kalustau.cache.Cache;
import ru.clevertec.kalustau.model.News;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class NewsCacheLFU implements Cache<News> {

    /** Map with user IDs and users */
    private Map<Long, News> valueMap = new HashMap<>();
    /** Map with user IDs and count of usage */
    private Map<Long, Integer> countMap = new HashMap<>();
    /** Map with count of usage and list with users */
    private TreeMap<Integer, List<News>> frequencyMap = new TreeMap<>();
    /** Field max collection size */
    private final int maxSize;

    /**
     * Constructor - new User Dao Cache LFU object
     * @param maxCacheSize - max size
     */
    public NewsCacheLFU(int maxCacheSize) {
        this.maxSize = maxCacheSize;
    }

    /**
     * Get user by id
     * @param id - user id
     * @return - User with given id or null if the user is not present
     */
    @Override
    public Optional<News> getById(Long id) {
        if (!valueMap.containsKey(id) || maxSize == 0) return Optional.empty();

        int frequency = countMap.get(id);
        News newsValue = frequencyMap.get(frequency).stream().filter(news -> news.getId() == id)
                .findFirst().get();
       frequencyMap.get(frequency).remove(newsValue);
       if (frequencyMap.get(frequency).size() == 0)
           frequencyMap.remove(frequency);

       frequencyMap.computeIfAbsent(frequency + 1, v -> new LinkedList<>()).add(newsValue);
       countMap.put(id, frequency + 1);
       return Optional.of(valueMap.get(id));
   }

    /**
     * Save new User in collections. If user present, update.
     * @param news user to save
     */
   @Override
   public void save(News news) {
        if (valueMap.containsKey(news.getId()) == false && maxSize > 0) {
            if (valueMap.size() == maxSize) {
                Integer lowestCount = frequencyMap.firstKey();
                News newsToDelete = frequencyMap.get(lowestCount).remove(0);

                if (frequencyMap.get(lowestCount).size() == 0)
                    frequencyMap.remove(lowestCount);

                valueMap.remove(newsToDelete.getId());
                countMap.remove(newsToDelete.getId());
            }

            valueMap.put(news.getId(), news);
            countMap.put(news.getId(), 1);
            frequencyMap.computeIfAbsent(1, v -> new LinkedList<>()).add(news);
        } else if (maxSize > 0) {
                valueMap.put(news.getId(), news);

                Integer frequency = countMap.get(news.getId());
                News newsValue = frequencyMap.get(frequency).stream().filter(n -> n.getId() == news.getId())
                        .findFirst().get();
                frequencyMap.get(frequency).remove(newsValue);

                if (frequencyMap.get(frequency).size() == 0)
                    frequencyMap.remove(frequency);

                frequencyMap.computeIfAbsent(frequency + 1, v -> new LinkedList<>()).add(newsValue);
                countMap.put(news.getId(), frequency + 1);
            }
        }

    /**
     * Update User in collections. If user is not present, creates new one.
     * @param news user to update
     */
    @Override
    public void update(News news) {
        save(news);
    }

    /**
     * Delete User from all collections
     * @param id id to delete
     */
    @Override
    public void delete(Long id) {
        News news = valueMap.get(id);
        valueMap.remove(id);
        int frequency = countMap.get(id);
        countMap.remove(id);
        frequencyMap.get(frequency).remove(news);
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
