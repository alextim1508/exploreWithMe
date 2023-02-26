package ru.practicum.explorewithme.main.server.compilation.service;


import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.explorewithme.main.server.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.main.server.compilation.dto.NewCompilationDto;
import ru.practicum.explorewithme.main.server.compilation.dto.UpdateCompilationDto;
import ru.practicum.explorewithme.main.server.compilation.model.Compilation;
import ru.practicum.explorewithme.main.server.event.service.EventFactory;
import ru.practicum.explorewithme.main.server.event.service.EventMapper;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {EventFactory.class, EventMapper.class}
)
public interface CompilationMapper {

    CompilationDto toDto(Compilation compilation);

    List<CompilationDto> toDto(List<Compilation> compilations);

    Compilation toEntity(NewCompilationDto compilationDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(UpdateCompilationDto compilationDto, @MappingTarget Compilation compilation);
}

