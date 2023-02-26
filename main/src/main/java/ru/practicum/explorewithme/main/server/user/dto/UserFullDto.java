package ru.practicum.explorewithme.main.server.user.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserFullDto {

    @NonNull
    private Long id;

    @NonNull
    private String email;

    @NonNull
    private String name;
}

