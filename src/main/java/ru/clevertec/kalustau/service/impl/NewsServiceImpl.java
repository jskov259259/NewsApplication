package ru.clevertec.kalustau.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.kalustau.dto.NewsDto;
import ru.clevertec.kalustau.mapper.NewsMapper;
import ru.clevertec.kalustau.model.News;
import ru.clevertec.kalustau.repository.NewsDao;
import ru.clevertec.kalustau.service.NewsService;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class NewsServiceImpl implements NewsService {

    private final NewsDao newsDao;
    private final NewsMapper newsMapper;

    @Override
    public List<NewsDto> findAll(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<News> pagedResult = newsDao.findAll(paging);

        return pagedResult.getContent().stream()
                .map(newsMapper::newsToDto)
                .collect(Collectors.toList());
    }

    @Override
    public NewsDto findById(Long id) {
        News news = newsDao.findById(id)
                .orElseThrow(() -> new RuntimeException("No such news with id=" + id));
        return newsMapper.newsToDto(news);
    }

    @Override
    @Transactional
    public NewsDto save(NewsDto newsDto) {
        News news = newsMapper.dtoToNews(newsDto);
        news.setTime(LocalTime.now());
        News createdNews = newsDao.save(news);
        return newsMapper.newsToDto(createdNews);
    }

    @Override
    @Transactional
    public NewsDto update(NewsDto newsDto) {
        News currentNews = newsDao.findById(newsDto.getId())
                .orElseThrow(() -> new RuntimeException("No such news with id=" + newsDto.getId()));

        News newNews = newsMapper.dtoToNews(newsDto);
        updateNews(currentNews, newNews);
        News updatedNews = newsDao.save(currentNews);
        return newsMapper.newsToDto(updatedNews);
    }

    @Override
    @Transactional
    public void deleteById(Long tagId) {
        newsDao.deleteById(tagId);
    }

    private void updateNews(News currentNews, News newNews) {
        currentNews.setTitle(newNews.getTitle());
        currentNews.setText(newNews.getText());
        if (Objects.nonNull(newNews.getComments()))
            newNews.getComments().stream().forEach(currentNews::addComment);
    }
}
