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

import java.util.List;
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

}
