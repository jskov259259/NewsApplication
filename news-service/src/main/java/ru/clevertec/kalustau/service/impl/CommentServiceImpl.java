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
import ru.clevertec.kalustau.exceptions.PermissionException;
import ru.clevertec.kalustau.exceptions.ResourceNotFoundException;
import ru.clevertec.kalustau.model.Comment;
import ru.clevertec.kalustau.model.News;
import ru.clevertec.kalustau.repository.CommentRepository;
import ru.clevertec.kalustau.repository.NewsRepository;
import ru.clevertec.kalustau.service.CommentService;
import ru.clevertec.kalustau.util.EntitySpecificationsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
    private final UserUtility userUtility;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Comment> findAll(String search, Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Specification<Comment> specification = new EntitySpecificationsBuilder<Comment>().getSpecification(search);

        Page<Comment> pagedResult = commentRepository.findAll(specification, paging);

        return pagedResult.getContent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(value = "comment", key = "#id")
    public Comment findById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No such comment with id=" + id));
        return comment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Comment> findAllByNewsId(Long newsId, Integer pageNo, Integer pageSize, String sortBy) {
        if (!newsRepository.existsById(newsId)) {
            throw new ResourceNotFoundException("Not found News with id = " + newsId);
        }
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Comment> pagedResult = commentRepository.findAllByNewsId(newsId, paging);

        return pagedResult.getContent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    @CachePut(value = "comment", key = "#result.id")
    public Comment save(Long newsId, Comment comment, String token) {
        User user = userUtility.getUserByToken(token);
        if (!(isUserAdmin(user) || isUserSubscriber(user) || isUserJournalist(user))) {
            throw new PermissionException("No permission to perform operation");
        }

        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException("No such news with id=" + newsId));
        comment.setNews(news);
        comment.setTime(LocalDateTime.now());
        comment.setUserName(user.getUsername());

        Comment createdComment = commentRepository.save(comment);
        return createdComment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    @CachePut(value = "comment", key = "#id")
    public Comment update(Long id, Comment newComment, String token) {
        User user = userUtility.getUserByToken(token);
        if (!(isUserAdmin(user) || isUserSubscriber(user) || isUserJournalist(user))) {
            throw new PermissionException("No permission to perform operation");
        }
        Comment currentComment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No such comment with id=" + id));

        if (!isUserHasRightsToModification(user, currentComment.getUserName())) {
            throw new PermissionException("No permission to perform operation");
        }

        updateComment(currentComment, newComment);
        Comment updatedComment = commentRepository.save(currentComment);
        return updatedComment;
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
