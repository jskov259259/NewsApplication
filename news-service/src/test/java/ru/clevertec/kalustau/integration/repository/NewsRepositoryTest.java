package ru.clevertec.kalustau.integration.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import ru.clevertec.kalustau.integration.BaseIntegrationTest;
import ru.clevertec.kalustau.model.News;
import ru.clevertec.kalustau.repository.NewsRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.clevertec.kalustau.util.Constants.TEST_ID;
import static ru.clevertec.kalustau.util.Constants.TEST_PAGE_NO;
import static ru.clevertec.kalustau.util.Constants.TEST_PAGE_SIZE;
import static ru.clevertec.kalustau.util.Constants.TEST_SEARCH;
import static ru.clevertec.kalustau.util.Constants.TEST_SORT_BY;
import static ru.clevertec.kalustau.util.TestData.getNews;
import static ru.clevertec.kalustau.util.TestData.getTestSpecification;

class NewsRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private NewsRepository newsRepository;

    @Test
    void checkFindAllShouldReturn10() {
        Specification<News> specification = getTestSpecification(TEST_SEARCH);
        Page<News> pagedResult = newsRepository.findAll(specification,
                PageRequest.of(TEST_PAGE_NO, TEST_PAGE_SIZE, Sort.by(TEST_SORT_BY)));
        assertThat(pagedResult.getContent().size()).isEqualTo(10);
    }

    @Test
    void checkFindByIdShouldReturnOptionalNews() {
        Optional<News> newsData = newsRepository.findById(TEST_ID);
        assertThat(newsData.get().getId()).isEqualTo(TEST_ID);
        assertThat(newsData.get().getTitle()).isEqualTo("The secret of the presidential cue card");
    }

    @Test
    void checkFindByIdShouldReturnOptionalEmpty() {
        Optional<News> newsData = newsRepository.findById(11L);
        assertThat(newsData).isEmpty();
    }

    @Test
    void checkSave() {
        News news = getNews();
        News createdNews = newsRepository.save(news);
        assertThat(createdNews.getTitle()).isEqualTo("Title2");
        assertThat(createdNews.getText()).isEqualTo("Text2");
    }

    @Test
    void checkUpdate() {
        News news = getNews();
        newsRepository.save(news);

        news.setTitle("New title");
        news.setText("New text");
        News updatedNews = newsRepository.save(news);
        assertThat(updatedNews.getTitle()).isEqualTo("New title");
        assertThat(updatedNews.getText()).isEqualTo("New text");
    }

    @Test
    void deleteById() {
        Integer sizeBefore = newsRepository.findAll(PageRequest.of(TEST_PAGE_NO, TEST_PAGE_SIZE, Sort.by(TEST_SORT_BY)))
                .getContent().size();

        newsRepository.deleteById(10L);

        Integer sizeAfter = newsRepository.findAll(PageRequest.of(TEST_PAGE_NO, TEST_PAGE_SIZE, Sort.by(TEST_SORT_BY)))
                .getContent().size();
        assertThat(sizeAfter).isLessThan(sizeBefore);
    }

}

