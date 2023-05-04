package ru.clevertec.kalustau.service;

import java.util.List;
import ru.clevertec.kalustau.dto.Proto.NewsDto;

public interface NewsService {

    List<NewsDto> findAll(String search, Integer pageNo, Integer pageSize, String sortBy);

    NewsDto findById(Long id);

    NewsDto save(NewsDto newsDto);

    NewsDto update(Long id, NewsDto newsDto);

    void deleteById(Long id);
}
