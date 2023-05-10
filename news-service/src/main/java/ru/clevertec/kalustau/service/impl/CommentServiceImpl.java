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
import ru.clevertec.kalustau.client.dto.User;
import ru.clevertec.kalustau.dto.CommentDtoRequest;
import ru.clevertec.kalustau.exceptions.PermissionException;
import ru.clevertec.kalustau.exceptions.ResourceNotFoundException;
import ru.clevertec.kalustau.mapper.CommentMapper;
import ru.clevertec.kalustau.model.Comment;
import ru.clevertec.kalustau.model.News;
import ru.clevertec.kalustau.repository.CommentRepository;
import ru.clevertec.kalustau.repository.NewsRepository;
import ru.clevertec.kalustau.service.CommentService;
import ru.clevertec.kalustau.util.EntitySpecificationsBuilder;
import ru.clevertec.kalustau.dto.Proto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.clevertec.kalustau.service.impl.UserUtility.isUserAdmin;
import static ru.clevertec.kalustau.service.impl.UserUtility.isUserHasRightsToModification;
import static ru.clevertec.kalustau.service.impl.UserUtility.isUserJournalist;
import static ru.clevertec.kalustau.service.impl.UserUtility.isUserSubscriber;

/**
 * Service implementation for managing comments.
 * @author Dzmitry Kalustau
 */
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
@CacheConfig(cacheNames = "commentCache")
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final NewsRepository newsRepository;
    private final CommentMapper commentMapper;
    private final UserUtility userUtility;

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable
    public List<Proto.CommentDtoResponse> findAll(String search, Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Specification<Comment> specification = new EntitySpecificationsBuilder<Comment>().getSpecification(search);

        Page<Comment> pagedResult = commentRepository.findAll(specification, paging);

        return pagedResult.getContent().stream()
                .map(commentMapper::commentToDto)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(key = "#id")
    public Proto.CommentDtoResponse findById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No such comment with id=" + id));
        return commentMapper.commentToDto(comment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Proto.CommentDtoResponse> findAllByNewsId(Long newsId, Integer pageNo, Integer pageSize, String sortBy) {
        if (!newsRepository.existsById(newsId)) {
            throw new ResourceNotFoundException("Not found News with id = " + newsId);
        }
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Comment> pagedResult = commentRepository.findAllByNewsId(newsId, paging);

        return pagedResult.getContent().stream()
                .map(commentMapper::commentToDto)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    @CachePut(key = "#commentDtoRequest")
    public Proto.CommentDtoResponse save(Long newsId, CommentDtoRequest commentDtoRequest, String token) {
        User user = userUtility.getUserByToken(token);
        if (!(isUserAdmin(user) || isUserSubscriber(user) || isUserJournalist(user))) {
            throw new PermissionException("No permission to perform operation");
        }
        Comment comment = commentMapper.dtoToComment(commentDtoRequest);
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException("No such news with id=" + newsId));
        comment.setNews(news);
        comment.setTime(LocalDateTime.now());
        comment.setUserName(user.getUsername());

        Comment createdComment = commentRepository.save(comment);
        return commentMapper.commentToDto(createdComment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    @CachePut(key = "#commentDtoRequest.id")
    public Proto.CommentDtoResponse update(Long id, CommentDtoRequest commentDtoRequest, String token) {
        User user = userUtility.getUserByToken(token);
        if (!(isUserAdmin(user) || isUserSubscriber(user) || isUserJournalist(user))) {
            throw new PermissionException("No permission to perform operation");
        }
        Comment currentComment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No such comment with id=" + id));

        if (!isUserHasRightsToModification(user, currentComment.getUserName())) {
            throw new PermissionException("No permission to perform operation");
        }

        Comment newComment = commentMapper.dtoToComment(commentDtoRequest);
        updateComment(currentComment, newComment);
        Comment updatedComment = commentRepository.save(currentComment);
        return commentMapper.commentToDto(updatedComment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    @CacheEvict(key = "#id")
    public void deleteById(Long id, String token) {
        User user = userUtility.getUserByToken(token);
        if (!(isUserAdmin(user) || isUserSubscriber(user) || isUserJournalist(user))) {
            throw new PermissionException("No permission to perform operation");
        }
        Comment commentToDelete = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No such comment with id=" + id));

        if (!isUserHasRightsToModification(user, commentToDelete.getUserName())) {
            throw new PermissionException("No permission to perform operation");
        }

        commentRepository.deleteById(id);
    }

    private void updateComment(Comment currentComment, Comment newComment) {
        if (Objects.nonNull(newComment.getText())) currentComment.setText(newComment.getText());
    }

}
