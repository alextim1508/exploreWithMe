package ru.practicum.explorewithme.main.server.category.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryDto {

    @Size(max = 255, message = "{category.name.length}")
    @NotBlank(message = "{category.name_is_not_blank}")
    private String name;
}
