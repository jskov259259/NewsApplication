package ru.clevertec.kalustau.mapper;

import com.google.protobuf.Timestamp;
import org.springframework.stereotype.Component;
import ru.clevertec.kalustau.dto.NewsDtoRequest;
import ru.clevertec.kalustau.dto.Proto;
import ru.clevertec.kalustau.model.News;

import java.time.Instant;
import java.time.ZoneId;

@Component
public class NewsMapper {

    public News dtoToNews(NewsDtoRequest newsDtoRequest) {
        return News.builder()
                .title(newsDtoRequest.getTitle())
                .text(newsDtoRequest.getText())
                .build();
    }

    public Proto.NewsDtoResponse newsToDto(News news) {
        Instant instant = news.getTime().atZone(ZoneId.systemDefault()).toInstant();
        return Proto.NewsDtoResponse
                .newBuilder()
                .setId(news.getId())
                .setTitle(news.getTitle())
                .setText(news.getText())
                .setTime(Timestamp.newBuilder()
                        .setSeconds(instant.getEpochSecond())
                        .setNanos(instant.getNano())
                        .build())
                .build();
    }
}