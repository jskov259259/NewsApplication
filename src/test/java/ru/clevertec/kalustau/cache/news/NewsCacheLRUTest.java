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

class NewsCacheLRUTest {

    private static NewsCacheLRU newsCacheLRU;

    @BeforeAll
    static void setUp() {
        newsCacheLRU = new NewsCacheLRU(3);
    }

    @BeforeEach
    void init() {
        newsCacheLRU.clear();
        getNewsList().stream().forEach(newsCacheLRU::save);
    }

    @Test
    void checkGetById() {
        News expectedNews = getNewsList().get(0);
        Optional<News> newsData = newsCacheLRU.getById(TEST_ID);
        assertThat(newsData.get()).isEqualTo(expectedNews);
    }

    @Test
    void checkGetUserByIdShouldReturnOptionalEmpty() {
        Optional<News> actualNewsData = newsCacheLRU.getById(4L);
        assertThat(actualNewsData).isEmpty();
    }

    @Test
    void checkSave() {
        News expectedNews = getNews();
        newsCacheLRU.save(expectedNews);
        Optional<News> actualNewsData = newsCacheLRU.getById(expectedNews.getId());
        assertThat(actualNewsData.get()).isEqualTo(expectedNews);
    }

    @Test
    void checkUpdate() {
        News news = newsCacheLRU.getById(1L).get();
        news.setTitle("New title");
        news.setText("New text");
        newsCacheLRU.update(news);
        News updatedNews = newsCacheLRU.getById(1L).get();
        assertThat(updatedNews.getTitle()).isEqualTo("New title");
        assertThat(updatedNews.getText()).isEqualTo("New text");
    }

    @Test
    void checkDelete() {
        News news = getNewsList().get(0);
        newsCacheLRU.delete(news.getId());
        Optional<News> actualNewsData = newsCacheLRU.getById(news.getId());
        assertThat(actualNewsData).isEmpty();
    }

    @Test
    void checkCacheShouldReplaceNewsWithLongAgoUsage() {
        News news = News.builder().id(4L).title("New title").text("New text").build();
        newsCacheLRU.save(news);
        assertThat(newsCacheLRU.getById(1L)).isEmpty();
    }

}