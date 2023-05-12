package ru.clevertec.kalustau.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO entity representing the comment in request
 * @author Dzmitry Kalustau
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDtoRequest {

    @NotBlank(message = "Text is mandatory")
    private String text;

}
