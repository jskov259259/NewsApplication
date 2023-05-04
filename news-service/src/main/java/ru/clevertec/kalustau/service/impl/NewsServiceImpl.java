package ru.clevertec.kalustau.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import ru.clevertec.kalustau.mapper.NewsMapper;
import ru.clevertec.kalustau.model.News;
import ru.clevertec.kalustau.repository.NewsRepository;
import ru.clevertec.kalustau.service.NewsService;
import ru.clevertec.kalustau.util.EntitySpecificationsBuilder;
import ru.clevertec.kalustau.dto.Proto.NewsDto;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
@CacheConfig(cacheNames = "newsCache")
public class NewsServiceImpl implements NewsService {

    private static final Logger logger = LoggerFactory.getLogger(NewsServiceImpl.class);

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;

    @Override
    @Cacheable(cacheNames = "newsList")
    public List<NewsDto> findAll(String search, Integer pageNo, Integer pageSize, String sortBy) {
        logger.debug("findAll({}, {}, {})", pageNo, pageSize, sortBy);

        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Specification<News> specification = new EntitySpecificationsBuilder<News>().getSpecification(search);

        Page<News> pagedResult = newsRepository.findAll(specification, paging);

        return pagedResult.getContent().stream()
                .map(newsMapper::newsToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(key = "#id")
    public NewsDto findById(Long id) {
        logger.debug("findById({})", id);
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No such news with id=" + id));
        return newsMapper.newsToDto(news);
    }

    @Override
    @Transactional
    @CachePut(key = "#newsDto")
    public NewsDto save(NewsDto newsDto) {
        News news = newsMapper.dtoToNews(newsDto);
        news.setTime(LocalDateTime.now());
        News createdNews = newsRepository.save(news);
        return newsMapper.newsToDto(createdNews);
    }


    @Override
    @Transactional
    @CachePut(key = "#newsDto.id")
    public NewsDto update(Long id, NewsDto newsDto) {
        News currentNews = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No such news with id=" + newsDto.getId()));

        News newNews = newsMapper.dtoToNews(newsDto);
        updateNews(currentNews, newNews);
        News updatedNews = newsRepository.save(currentNews);
        return newsMapper.newsToDto(updatedNews);
    }

    @Override
    @Transactional
    @CacheEvict(key = "#id")
    public void deleteById(Long id) {
        newsRepository.deleteById(id);
    }

    private void updateNews(News currentNews, News newNews) {
        if (Objects.nonNull(newNews.getTitle())) currentNews.setTitle(newNews.getTitle());
        if (Objects.nonNull(newNews.getText())) currentNews.setText(newNews.getText());
    }

}
