package ru.clevertec.kalustau.service;

import ru.clevertec.kalustau.dto.Proto.CommentDto;

import java.util.List;

public interface CommentService {

    List<CommentDto> findAll(String search, Integer pageNo, Integer pageSize, String sortBy);

    CommentDto findById(Long id);

    List<CommentDto> findAllByNewsId(Long newsId, Integer pageNo, Integer pageSize, String sortBy);

    CommentDto save(Long newsId, CommentDto commentDto);

    CommentDto update(Long id, CommentDto commentDto);

    void deleteById(Long id);

}
