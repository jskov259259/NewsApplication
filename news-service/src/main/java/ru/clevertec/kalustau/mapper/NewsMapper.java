package ru.clevertec.kalustau.mapper;

import com.google.protobuf.Timestamp;
import org.springframework.stereotype.Component;
import ru.clevertec.kalustau.model.News;
import ru.clevertec.kalustau.dto.Proto.NewsDto;

import java.time.Instant;
import java.time.ZoneId;

@Component
public class NewsMapper {

    public News dtoToNews(NewsDto newsDTO) {
        return News.builder()
                .id(newsDTO.getId())
                .title(newsDTO.getTitle())
                .text(newsDTO.getText())
                .build();
    }

    public NewsDto newsToDto(News news) {
        Instant instant = news.getTime().atZone(ZoneId.systemDefault()).toInstant();
        return NewsDto
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