package ru.clevertec.kalustau.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsDtoRequest {

    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "Text is mandatory")
    private String text;

}
