package ru.clevertec.kalustau.util;

import ru.clevertec.kalustau.model.News;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TestData {

    public static List<News> getNewsList() {
        List<News> news = new ArrayList<>();
        news.add(News.builder().id(1L).time(LocalTime.now()).title("Title1")
                .text("Text1").build());
        news.add(News.builder().id(2L).time(LocalTime.now()).title("Title2")
                .text("Text2").build());
        news.add(News.builder().id(3L).time(LocalTime.now()).title("Title3")
                .text("Text3").build());
        return news;
    }

    public static News getNews() {
        return getNewsList().get(1);
    }

}