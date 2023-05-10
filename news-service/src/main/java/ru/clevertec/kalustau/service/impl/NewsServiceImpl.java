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
import ru.clevertec.kalustau.dto.NewsDtoRequest;
import ru.clevertec.kalustau.exceptions.PermissionException;
import ru.clevertec.kalustau.exceptions.ResourceNotFoundException;
import ru.clevertec.kalustau.mapper.NewsMapper;
import ru.clevertec.kalustau.model.News;
import ru.clevertec.kalustau.repository.NewsRepository;
import ru.clevertec.kalustau.service.NewsService;
import ru.clevertec.kalustau.util.EntitySpecificationsBuilder;
import ru.clevertec.kalustau.dto.Proto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
@CacheConfig(cacheNames = "newsCache")
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;
    private final UserUtility userUtility;

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(cacheNames = "newsList")
    public List<Proto.NewsDtoResponse> findAll(String search, Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Specification<News> specification = new EntitySpecificationsBuilder<News>().getSpecification(search);

        Page<News> pagedResult = newsRepository.findAll(specification, paging);

        return pagedResult.getContent().stream()
                .map(newsMapper::newsToDto)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(key = "#id")
    public Proto.NewsDtoResponse findById(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No such news with id=" + id));
        return newsMapper.newsToDto(news);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    @CachePut(key = "#newsDtoRequest")
    public Proto.NewsDtoResponse save(NewsDtoRequest newsDtoRequest, String token) {
        User user = userUtility.getUserByToken(token);
        if (!(isUserAdmin(user) || isUserJournalist(user))) {
            throw new PermissionException("No permission to perform operation");
        }
        News news = newsMapper.dtoToNews(newsDtoRequest);
        news.setTime(LocalDateTime.now());
        news.setUserName(user.getUsername());
        News createdNews = newsRepository.save(news);
        return newsMapper.newsToDto(createdNews);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    @CachePut(key = "#newsDtoRequest.id")
    public Proto.NewsDtoResponse update(Long id, NewsDtoRequest newsDtoRequest, String token) {
        User user = userUtility.getUserByToken(token);
        if (!(isUserAdmin(user) || isUserJournalist(user))) {
            throw new PermissionException("No permission to perform operation");
        }

        News currentNews = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No such news with id=" + id));

        if (!isUserHasRightsToModification(user, currentNews.getUserName())) {
            throw new PermissionException("No permission to perform operation");
        }

        News newNews = newsMapper.dtoToNews(newsDtoRequest);
        updateNews(currentNews, newNews);
        News updatedNews = newsRepository.save(currentNews);
        return newsMapper.newsToDto(updatedNews);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    @CacheEvict(key = "#id")
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
