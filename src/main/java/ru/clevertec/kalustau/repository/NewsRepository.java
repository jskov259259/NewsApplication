package ru.clevertec.kalustau.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.kalustau.model.News;

public interface NewsRepository extends JpaRepository<News, Long> {
}