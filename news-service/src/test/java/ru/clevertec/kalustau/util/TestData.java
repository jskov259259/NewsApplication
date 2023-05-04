package ru.clevertec.kalustau.util;

import com.google.common.base.Joiner;
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.Struct;
import com.google.protobuf.util.JsonFormat;
import org.springframework.data.jpa.domain.Specification;
import ru.clevertec.kalustau.dto.criteria.SearchOperation;
import ru.clevertec.kalustau.model.BaseEntity;
import ru.clevertec.kalustau.model.Comment;
import ru.clevertec.kalustau.model.News;
import ru.clevertec.kalustau.dto.Proto.NewsDto;
import ru.clevertec.kalustau.dto.Proto.CommentDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        return NewsDto.newBuilder().setId(1L).setTitle("Title1").setText("Text1").build();
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
        return CommentDto.newBuilder().setId(1L).setText("Text1")
                .setUserName("User1").build();
    }

    public static <E extends BaseEntity<Long>> Specification<E> getTestSpecification(String search) {
        EntitySpecificationsBuilder builder = new EntitySpecificationsBuilder();
        String operationSetExper = Joiner.on("|").join(SearchOperation.SIMPLE_OPERATION_SET);
        Pattern pattern = Pattern.compile("(\\w+?)(" + operationSetExper + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(
                    matcher.group(1),
                    matcher.group(2),
                    matcher.group(4),
                    matcher.group(3),
                    matcher.group(5));
        }
        return builder.build();
    }

    public static Message fromJson(String json) throws IOException {
        Message.Builder structBuilder = Struct.newBuilder();
        JsonFormat.parser().ignoringUnknownFields().merge(json, structBuilder);
        return structBuilder.build();
    }

    public static String toJson(MessageOrBuilder messageOrBuilder) throws IOException {
        return JsonFormat.printer().print(messageOrBuilder);
    }

}