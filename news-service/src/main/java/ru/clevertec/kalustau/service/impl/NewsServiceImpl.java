package ru.clevertec.kalustau.service.impl;

import lombok.RequiredArgsConstructor;

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
import ru.clevertec.kalustau.model.News;
import ru.clevertec.kalustau.repository.NewsRepository;
import ru.clevertec.kalustau.service.NewsService;
import ru.clevertec.kalustau.util.EntitySpecificationsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static ru.clevertec.kalustau.service.impl.UserUtility.isUserAdmin;
import static ru.clevertec.kalustau.service.impl.UserUtility.isUserHasRightsToModification;
import static ru.clevertec.kalustau.service.impl.UserUtility.isUserJournalist;

/**
 * Service implementation for managing news.
 * @author Dzmitry Kalustau
 */
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final UserUtility userUtility;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<News> findAll(String search, Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Specification<News> specification = new EntitySpecificationsBuilder<News>().getSpecification(search);

        Page<News> pagedResult = newsRepository.findAll(specification, paging);

        return pagedResult.getContent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(value = "news", key = "#id")
    public News findById(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No such news with id=" + id));
        return news;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    @CachePut(value = "news", key = "#result.id")
    public News save(News news, String token) {
        User user = userUtility.getUserByToken(token);
        if (!(isUserAdmin(user) || isUserJournalist(user))) {
            throw new PermissionException("No permission to perform operation");
        }
        news.setTime(LocalDateTime.now());
        news.setUserName(user.getUsername());
        News createdNews = newsRepository.save(news);
        return createdNews;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    @CachePut(value = "news", key = "#id")
    public News update(Long id, News newNews, String token) {
        User user = userUtility.getUserByToken(token);
        if (!(isUserAdmin(user) || isUserJournalist(user))) {
            throw new PermissionException("No permission to perform operation");
        }

        News currentNews = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No such news with id=" + id));

        if (!isUserHasRightsToModification(user, currentNews.getUserName())) {
            throw new PermissionException("No permission to perform operation");
        }

        updateNews(currentNews, newNews);
        News updatedNews = newsRepository.save(currentNews);
        return updatedNews;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    @CacheEvict(value = "news", key = "#id")
    public void deleteById(Long id, String token) {
        User user = userUtility.getUserByToken(token);
        if (!(isUserAdmin(user) || isUserJournalist(user))) {
            throw new PermissionException("No permission to perform operation");
        }
        News newsToDelete = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No such news with id=" + id));

        if (!isUserHasRightsToModification(user, newsToDelete.getUserName())) {
            throw new PermissionException("No permission to perform operation");
        }

        newsRepository.deleteById(id);
    }

    private void updateNews(News currentNews, News newNews) {
        if (Objects.nonNull(newNews.getTitle())) currentNews.setTitle(newNews.getTitle());
        if (Objects.nonNull(newNews.getText())) currentNews.setText(newNews.getText());
    }

}
