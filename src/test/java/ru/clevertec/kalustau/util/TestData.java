package ru.clevertec.kalustau.util;

import ru.clevertec.kalustau.model.Comment;
import ru.clevertec.kalustau.model.News;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

    public static List<Comment> getCommentList() {
        List<Comment> comments = new ArrayList<>();
        comments.add(Comment.builder().id(1L).time(LocalTime.now()).text("Text1")
                .userName("User1").build());
        comments.add(Comment.builder().id(2L).time(LocalTime.now()).text("Text2")
                .userName("User2").build());
        comments.add(Comment.builder().id(3L).time(LocalTime.now()).text("Text3")
                .userName("User3").build());
        return comments;
    }

    public static Comment getComment() {
        return getCommentList().get(1);
    }

}