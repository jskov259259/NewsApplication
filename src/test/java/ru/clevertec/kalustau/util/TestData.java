package ru.clevertec.kalustau.util;

import ru.clevertec.kalustau.dto.CommentDto;
import ru.clevertec.kalustau.dto.NewsDto;
import ru.clevertec.kalustau.model.Comment;
import ru.clevertec.kalustau.model.News;

import java.util.ArrayList;
import java.util.List;

public class TestData {

    public static List<News> getNewsList() {
        List<News> news = new ArrayList<>();
        news.add(News.builder().id(1L).title("Title1").text("Text1").build());
        news.add(News.builder().id(2L).title("Title2").text("Text2").build());
        news.add(News.builder().id(3L).title("Title3").text("Text3").build());
        return news;
    }

    public static News getNews() {
        return getNewsList().get(1);
    }

    public static NewsDto getNewsDto() {
        return NewsDto.builder().id(1L).title("Title1").text("Text1").build();
    }

    public static List<Comment> getCommentList() {
        List<Comment> comments = new ArrayList<>();
        comments.add(Comment.builder().id(1L).text("Text1")
                .userName("User1").build());
        comments.add(Comment.builder().id(2L).text("Text2")
                .userName("User2").build());
        comments.add(Comment.builder().id(3L).text("Text3")
                .userName("User3").build());
        return comments;
    }

    public static Comment getComment() {
        return getCommentList().get(1);
    }

    public static CommentDto getCommentDto() {
        return CommentDto.builder().id(1L).text("Text1")
                .userName("User1").build();
    }

}