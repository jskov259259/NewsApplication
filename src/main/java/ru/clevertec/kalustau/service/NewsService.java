package ru.clevertec.kalustau.service;

import ru.clevertec.kalustau.dto.NewsDto;

import java.util.List;

public interface NewsService {

    List<NewsDto> findAll(Integer pageNo, Integer pageSize, String sortBy);

    NewsDto findById(Long id);

    NewsDto save(NewsDto newsDto);

    NewsDto update(NewsDto newsDto);

    void deleteById(Long id);
}
