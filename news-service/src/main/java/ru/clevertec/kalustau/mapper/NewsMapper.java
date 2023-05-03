package ru.clevertec.kalustau.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.kalustau.dto.NewsDto;
import ru.clevertec.kalustau.model.News;

@Mapper(componentModel = "spring")
public interface NewsMapper {

    News dtoToNews(NewsDto newsDto);

    NewsDto newsToDto(News news);
}