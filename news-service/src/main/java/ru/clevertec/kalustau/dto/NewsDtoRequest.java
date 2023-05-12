package ru.clevertec.kalustau.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO entity representing the news in request
 * @author Dzmitry Kalustau
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsDtoRequest {

    @Schema(description = "News title", example = "COVID-19: The virus that shut down the world")
    @NotBlank(message = "Title is mandatory")
    private String title;

    @Schema(description = "News text",
            example = "The World Health Organization (WHO) declared an end to the COVID-19 global public health" +
                    " emergency on 3 May 2023, following more than three years of crisis")
    @NotBlank(message = "Text is mandatory")
    private String text;

}
