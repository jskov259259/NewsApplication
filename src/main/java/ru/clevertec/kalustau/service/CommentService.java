package ru.clevertec.kalustau.service;

import ru.clevertec.kalustau.dto.CommentDto;
import ru.clevertec.kalustau.dto.NewsDto;

import java.util.List;

public interface CommentService {

    List<CommentDto> findAll(Integer pageNo, Integer pageSize, String sortBy);

    CommentDto findById(Long id);

    List<CommentDto> findAllByNewsId(Long newsId, Integer pageNo, Integer pageSize, String sortBy);

    CommentDto save(Long newsId, CommentDto commentDto);

    CommentDto update(CommentDto commentDto);

    void deleteById(Long id);

}