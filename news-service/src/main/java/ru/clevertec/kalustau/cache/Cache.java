package ru.clevertec.kalustau.cache;

import ru.clevertec.kalustau.model.BaseEntity;

import java.util.Optional;

public interface Cache<E extends BaseEntity<Long>> {

    Optional<E> getById(Long id);

    void save(E e);

    void update(E e);

    void delete(Long id);

    void clear();
}
