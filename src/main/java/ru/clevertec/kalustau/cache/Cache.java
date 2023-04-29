package ru.clevertec.kalustau.cache;

import java.util.Optional;

public interface Cache<E> {

    Optional<E> getById(Long id);

    void save(E e);

    void update(E e);

    void delete(Long id);
}
