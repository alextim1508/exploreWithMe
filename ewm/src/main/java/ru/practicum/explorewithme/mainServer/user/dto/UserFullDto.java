package ru.practicum.explorewithme.mainServer.user.dto;

import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFullDto {

    @NonNull
    private Long id;

    @NonNull
    private String email;

    @NonNull
    private String name;
}

