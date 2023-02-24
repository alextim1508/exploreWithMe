package ru.practicum.explorewithme.main.server.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class NewUserDto {

    @NotNull
    @Email(message = "{user.email.not_valid}")
    @Size(max = 255, message = "{user.email.length}")
    @NotBlank(message = "{user.email.not_blank}")
    private String email;

    @NotNull
    @Size(max = 255, message = "{user.name.length}")
    @NotBlank(message = "{user.name.not_blank}")
    private String name;
}
