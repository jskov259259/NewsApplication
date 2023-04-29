package ru.clevertec.kalustau.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.Value;
import ru.clevertec.kalustau.model.Comment;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "title")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewsDto {

    Long id;

    LocalTime time;

    String title;

    String text;

    List<Comment> comments = new ArrayList<>();
}
