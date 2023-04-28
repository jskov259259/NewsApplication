package ru.clevertec.kalustau.dto;

import lombok.Value;
import ru.clevertec.kalustau.model.Comment;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Value
public class NewsDto {

    Long id;

    LocalTime time;

    String title;

    String text;

    List<Comment> comments = new ArrayList<>();
}
