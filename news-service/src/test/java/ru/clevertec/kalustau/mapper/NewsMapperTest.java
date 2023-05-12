package ru.clevertec.kalustau.mapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.clevertec.kalustau.dto.NewsDtoRequest;
import ru.clevertec.kalustau.dto.Proto;
import ru.clevertec.kalustau.model.News;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.clevertec.kalustau.util.TestData.getNewsDtoRequest;
import static ru.clevertec.kalustau.util.TestData.getNewsDtoResponse;
import static ru.clevertec.kalustau.util.TestData.getNewsList;

class NewsMapperTest {

    private static NewsMapper newsMapper;

    private News news = getNewsList().get(0);;
    private NewsDtoRequest newsDtoRequest = getNewsDtoRequest();
    private Proto.NewsDtoResponse newsDtoResponse = getNewsDtoResponse();

    @BeforeAll
    static void setUp() {
        newsMapper = new NewsMapper();
    }

    @Test
    void checkDtoToNews() {
        News actual = newsMapper.dtoToNews(newsDtoRequest);
        assertThat(actual.getTitle()).isEqualTo(news.getTitle());
        assertThat(actual.getText()).isEqualTo(news.getText());
    }

    @Test
    void checkNewsToDto() {
        Proto.NewsDtoResponse actual = newsMapper.newsToDto(news);
        assertThat(actual.getId()).isEqualTo(newsDtoResponse.getId());
        assertThat(actual.getTitle()).isEqualTo(newsDtoResponse.getTitle());
        assertThat(actual.getText()).isEqualTo(newsDtoResponse.getText());
    }

}