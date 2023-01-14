package ru.practicum.explorewithme.mainServer.user.dto;

import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @NonNull
    private Long id;

    @NonNull
    private String email;

    @NonNull
    private String name;
}

