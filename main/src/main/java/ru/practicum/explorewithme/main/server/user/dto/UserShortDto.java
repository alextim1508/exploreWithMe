package ru.practicum.explorewithme.main.server.user.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserShortDto {

    @NonNull
    private Long id;

    @NonNull
    private String name;
}
