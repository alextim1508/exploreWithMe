package ru.practicum.explorewithme.mainServer.compilation.service;


import org.mapstruct.Mapper;
import ru.practicum.explorewithme.mainServer.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.mainServer.compilation.dto.NewCompilationDto;
import ru.practicum.explorewithme.mainServer.compilation.model.Compilation;
import ru.practicum.explorewithme.mainServer.event.service.EventFactory;
import ru.practicum.explorewithme.mainServer.event.service.EventMapper;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {EventFactory.class, EventMapper.class}
)
public interface CompilationMapper {

    CompilationDto toDto(Compilation compilation);

    List<CompilationDto> toDto(List<Compilation> compilations);

    Compilation toEntity(NewCompilationDto compilationDto);
}

