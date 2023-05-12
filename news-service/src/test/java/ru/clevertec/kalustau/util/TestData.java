package ru.clevertec.kalustau.util;

import com.google.common.base.Joiner;
import com.google.protobuf.Timestamp;
import org.springframework.data.jpa.domain.Specification;
import ru.clevertec.kalustau.client.dto.Role;
import ru.clevertec.kalustau.client.dto.RoleEnum;
import ru.clevertec.kalustau.client.dto.User;
import ru.clevertec.kalustau.dto.CommentDtoRequest;
import ru.clevertec.kalustau.dto.NewsDtoRequest;
import ru.clevertec.kalustau.dto.criteria.SearchOperation;
import ru.clevertec.kalustau.model.BaseEntity;
import ru.clevertec.kalustau.model.Comment;
import ru.clevertec.kalustau.model.News;
import ru.clevertec.kalustau.dto.Proto;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestData {

    public static List<News> getNewsList() {
        List<News> news = new ArrayList<>();
        news.add(News.builder().id(1L).title("Title1").text("Text1").time(LocalTime.now()).userName("User1").build());
        news.add(News.builder().id(2L).title("Title2").text("Text2").time(LocalTime.now()).userName("User2").build());
        news.add(News.builder().id(3L).title("Title3").text("Text3").time(LocalTime.now()).userName("User3").build());
        return news;
    }

    public static News getNews() {
        return getNewsList().get(1);
    }

    public static NewsDtoRequest getNewsDtoRequest() {
        return NewsDtoRequest.builder().title("Title1").text("Text1").build();
    }

    public static Proto.NewsDtoResponse getNewsDtoResponse() {
        Instant instant = LocalTime.now().atDate(LocalDate.now()).toInstant(ZoneOffset.UTC);
        return Proto.NewsDtoResponse
                .newBuilder()
                .setId(1L)
                .setTitle("Title1")
                .setText("Text1")
                .setTime(Timestamp.newBuilder()
                        .setSeconds(instant.getEpochSecond())
                        .setNanos(instant.getNano())
                        .build())
                .build();
    }

    public static List<Comment> getCommentList() {
        List<Comment> comments = new ArrayList<>();
        comments.add(Comment.builder().id(1L).text("Text1").time(LocalTime.now())
                .userName("User1").build());
        comments.add(Comment.builder().id(2L).text("Text2").time(LocalTime.now())
                .userName("User2").build());
        comments.add(Comment.builder().id(3L).text("Text3").time(LocalTime.now())
                .userName("User3").build());
        return comments;
    }

    public static Comment getComment() {
        return getCommentList().get(1);
    }

    public static CommentDtoRequest getCommentDtoRequest() {
        return CommentDtoRequest.builder().text("Text1").build();
    }

    public static Proto.CommentDtoResponse getCommentDtoResponse() {
        Instant instant = LocalTime.now().atDate(LocalDate.now()).toInstant(ZoneOffset.UTC);
        return Proto.CommentDtoResponse
                .newBuilder()
                .setId(1L)
                .setText("Text1")
                .setUserName("User1")
                .setTime(Timestamp.newBuilder()
                        .setSeconds(instant.getEpochSecond())
                        .setNanos(instant.getNano())
                        .build())
                .build();
    }

    public static User getUserAdmin() {
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(1L, RoleEnum.ADMIN));
        return User.builder().id(1L).username("User1").password("1234").firstName("Ivan").lastName("Ivanov")
                .roles(roles).build();
    }

    public static User getUserSubscriber() {
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(1L, RoleEnum.SUBSCRIBER));
        return User.builder().id(1L).username("User1").password("1234").firstName("Ivan").lastName("Ivanov")
                .roles(roles).build();
    }

    public static User getUserJournalist() {
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(1L, RoleEnum.JOURNALIST));
        return User.builder().id(1L).username("User1").password("1234").firstName("Ivan").lastName("Ivanov")
                .roles(roles).build();
    }

    public static User getUserWithoutRole() {
        return User.builder().id(1L).username("User1").password("1234").firstName("Ivan").lastName("Ivanov")
                .roles(new HashSet<>()).build();
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

}