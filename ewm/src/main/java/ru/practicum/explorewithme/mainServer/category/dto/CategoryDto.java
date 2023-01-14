package ru.practicum.explorewithme.mainServer.category.dto;

import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    @NonNull
    private Long id;

    @NonNull
    private String name;
}