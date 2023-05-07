package ru.clevertec.kalustau.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.clevertec.kalustau.model.News;

/**
 * Repository interface for News entity.
 * Provides methods for interacting with the database
 * @author Dzmitry Kalustau
 */
public interface NewsRepository extends JpaRepository<News, Long>, JpaSpecificationExecutor<News> {
}
