package ru.practicum.explorewithme.main.server.category.dto;

import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CategoryDto {

    @NonNull
    private Long id;

    @NonNull
    private String name;
}