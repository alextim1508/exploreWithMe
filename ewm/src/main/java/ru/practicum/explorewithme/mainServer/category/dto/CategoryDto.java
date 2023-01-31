package ru.practicum.explorewithme.mainServer.category.dto;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    @NonNull
    private Long id;

    @NonNull
    private String name;
}