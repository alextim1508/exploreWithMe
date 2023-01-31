package ru.practicum.explorewithme.mainServer.category.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryDto {

    @NonNull
    private Long id;

    @NotBlank
    @Size(max = 255)
    private String name;
}