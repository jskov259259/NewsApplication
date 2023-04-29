package ru.clevertec.kalustau.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.kalustau.dto.CommentDto;
import ru.clevertec.kalustau.mapper.CommentMapper;
import ru.clevertec.kalustau.model.Comment;
import ru.clevertec.kalustau.model.News;
import ru.clevertec.kalustau.repository.CommentDao;
import ru.clevertec.kalustau.repository.NewsDao;
import ru.clevertec.kalustau.service.CommentService;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentDao commentDao;
    private final NewsDao newsDao;
    private final CommentMapper commentMapper;

    @Override
    public List<CommentDto> findAll(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Comment> pagedResult = commentDao.findAll(paging);

        return pagedResult.getContent().stream()
                .map(commentMapper::commentToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto findById(Long id) {
        Comment comment = commentDao.findById(id)
                .orElseThrow(() -> new RuntimeException("No such comment with id=" + id));
        return commentMapper.commentToDto(comment);
    }

    @Override
    public List<CommentDto> findAllByNewsId(Long newsId, Integer pageNo, Integer pageSize, String sortBy) {
        if (!newsDao.existsById(newsId)) {
            throw new RuntimeException("Not found News with id = " + newsId);
        }
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Comment> pagedResult = commentDao.findAllByNewsId(newsId, paging);

        return pagedResult.getContent().stream()
                .map(commentMapper::commentToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto save(Long newsId, CommentDto commentDto) {
        Comment comment = commentMapper.dtoToComment(commentDto);
        News news = newsDao.findById(newsId)
                .orElseThrow(() -> new RuntimeException("No such news with id=" + newsId));
        comment.setNews(news);
        comment.setTime(LocalTime.now());

        Comment createdComment = commentDao.save(comment);
        return commentMapper.commentToDto(createdComment);
    }

    @Override
    @Transactional
    public CommentDto update(CommentDto commentDto) {
        Comment currentComment = commentDao.findById(commentDto.getId())
                .orElseThrow(() -> new RuntimeException("No such comment with id=" + commentDto.getId()));

        Comment newComment = commentMapper.dtoToComment(commentDto);
        updateComment(currentComment, newComment);
        Comment updatedComment = commentDao.save(currentComment);
        return commentMapper.commentToDto(updatedComment);
    }

    @Override
    @Transactional
    public void deleteById(Long tagId) {
        commentDao.deleteById(tagId);
    }

    private void updateComment(Comment currentComment, Comment newComment) {
        if (Objects.nonNull(newComment.getText())) currentComment.setText(newComment.getText());
        if (Objects.nonNull(newComment.getUserName())) currentComment.setUserName(newComment.getUserName());
    }
}
