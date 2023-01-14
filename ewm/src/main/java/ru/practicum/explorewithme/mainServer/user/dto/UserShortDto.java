package ru.practicum.explorewithme.mainServer.user.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserShortDto {

    @NonNull
    private Long id;

    @NonNull
    private String name;
}
