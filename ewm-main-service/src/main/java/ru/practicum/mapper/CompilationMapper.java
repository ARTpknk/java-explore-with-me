package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.model.compilation.Compilation;
import ru.practicum.model.compilation.NewCompilation;

import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {

    public NewCompilation toNewCompilation(NewCompilationDto dto) {
        return NewCompilation.builder()
                .id(dto.getId())
                .events(dto.getEvents())
                .pinned(dto.getPinned())
                .title(dto.getTitle())
                .build();
    }

    public CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .events(compilation.getEvents().stream().map(EventMapper::fromEventToEventShortDto)
                        .collect(Collectors.toSet()))
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }

    public Compilation updateCompilation(Compilation compilation, NewCompilation updateCompilation) {
        if (updateCompilation.getPinned() != null) {
            compilation.setPinned(updateCompilation.getPinned());
        }
        if (updateCompilation.getTitle() != null && updateCompilation.getTitle().isBlank()) {
            compilation.setTitle(updateCompilation.getTitle());
        }
        return compilation;
    }
}