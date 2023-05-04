package ru.clevertec.kalustau.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.kalustau.exceptions.ResourceNotFoundException;
import ru.clevertec.kalustau.mapper.CommentMapper;
import ru.clevertec.kalustau.model.Comment;
import ru.clevertec.kalustau.model.News;
import ru.clevertec.kalustau.repository.CommentRepository;
import ru.clevertec.kalustau.repository.NewsRepository;
import ru.clevertec.kalustau.service.CommentService;
import ru.clevertec.kalustau.util.EntitySpecificationsBuilder;
import ru.clevertec.kalustau.dto.Proto.CommentDto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
@CacheConfig(cacheNames = "commentCache")
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final NewsRepository newsRepository;
    private final CommentMapper commentMapper;

    @Override
    @Cacheable
    public List<CommentDto> findAll(String search, Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Specification<Comment> specification = new EntitySpecificationsBuilder<Comment>().getSpecification(search);

        Page<Comment> pagedResult = commentRepository.findAll(specification, paging);

        return pagedResult.getContent().stream()
                .map(commentMapper::commentToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(key = "#id")
    public CommentDto findById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No such comment with id=" + id));
        return commentMapper.commentToDto(comment);
    }

    @Override
    public List<CommentDto> findAllByNewsId(Long newsId, Integer pageNo, Integer pageSize, String sortBy) {
        if (!newsRepository.existsById(newsId)) {
            throw new ResourceNotFoundException("Not found News with id = " + newsId);
        }
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Comment> pagedResult = commentRepository.findAllByNewsId(newsId, paging);

        return pagedResult.getContent().stream()
                .map(commentMapper::commentToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @CachePut(key = "#commentDto")
    public CommentDto save(Long newsId, CommentDto commentDto) {
        Comment comment = commentMapper.dtoToComment(commentDto);
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException("No such news with id=" + newsId));
        comment.setNews(news);
        comment.setTime(LocalDateTime.now());

        Comment createdComment = commentRepository.save(comment);
        return commentMapper.commentToDto(createdComment);
    }

    @Override
    @Transactional
    @CachePut(key = "#commentDto.id")
    public CommentDto update(Long id, CommentDto commentDto) {
        Comment currentComment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No such comment with id=" + commentDto.getId()));

        Comment newComment = commentMapper.dtoToComment(commentDto);
        updateComment(currentComment, newComment);
        Comment updatedComment = commentRepository.save(currentComment);
        return commentMapper.commentToDto(updatedComment);
    }

    @Override
    @Transactional
    @CacheEvict(key = "#id")
    public void deleteById(Long tagId) {
        commentRepository.deleteById(tagId);
    }

    private void updateComment(Comment currentComment, Comment newComment) {
        if (Objects.nonNull(newComment.getText())) currentComment.setText(newComment.getText());
        if (Objects.nonNull(newComment.getUserName())) currentComment.setUserName(newComment.getUserName());
    }

}
