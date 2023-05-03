package ru.clevertec.kalustau.cache.news;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.kalustau.model.News;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.clevertec.kalustau.util.Constants.TEST_ID;
import static ru.clevertec.kalustau.util.TestData.getNews;
import static ru.clevertec.kalustau.util.TestData.getNewsList;

class NewsCacheLFUTest {

    private static NewsCacheLFU newsCacheLFU;

    @BeforeAll
    static void setUp() {
        newsCacheLFU = new NewsCacheLFU(3);
    }

    @BeforeEach
    void init() {
        newsCacheLFU.clear();
        getNewsList().stream().forEach(newsCacheLFU::save);
    }

    @Test
    void checkGetById() {
        News expectedNews = getNewsList().get(0);
        Optional<News> newsData = newsCacheLFU.getById(TEST_ID);
        assertThat(newsData.get()).isEqualTo(expectedNews);
    }

    @Test
    void checkGetUserByIdShouldReturnOptionalEmpty() {
        Optional<News> actualNewsData = newsCacheLFU.getById(4L);
        assertThat(actualNewsData).isEmpty();
    }

    @Test
    void checkSave() {
        News expectedNews = getNews();
        newsCacheLFU.save(expectedNews);
        Optional<News> actualNewsData = newsCacheLFU.getById(expectedNews.getId());
        assertThat(actualNewsData.get()).isEqualTo(expectedNews);
    }

    @Test
    void checkUpdate() {
        News news = newsCacheLFU.getById(1L).get();
        news.setTitle("New title");
        news.setText("New text");
        newsCacheLFU.update(news);
        News updatedNews = newsCacheLFU.getById(1L).get();
        assertThat(updatedNews.getTitle()).isEqualTo("New title");
        assertThat(updatedNews.getText()).isEqualTo("New text");
    }

    @Test
    void checkDelete() {
        News news = getNewsList().get(0);
        newsCacheLFU.delete(news.getId());
        Optional<News> actualNewsData = newsCacheLFU.getById(news.getId());
        assertThat(actualNewsData).isEmpty();
    }

    @Test
    void checkCacheShouldReplaceNewsWithLowestFrequently() {
        newsCacheLFU.getById(1L);
        newsCacheLFU.getById(2L);

        News news = News.builder().id(4L).title("New title").text("New text").build();
        newsCacheLFU.save(news);
        assertThat(newsCacheLFU.getById(3L)).isEmpty();
    }

}