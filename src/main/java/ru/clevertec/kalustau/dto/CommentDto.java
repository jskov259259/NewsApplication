package ru.clevertec.kalustau.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.clevertec.kalustau.model.News;

import java.time.LocalTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode()
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {

    private Long id;

    private LocalTime time;

    private String text;

    private String userName;

    private NewsDto newsDto;
}
