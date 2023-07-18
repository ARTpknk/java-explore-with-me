package ru.practicum.controller.adminApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequestDto;
import ru.practicum.exception.ExploreWithMeNotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.category.Category;
import ru.practicum.model.compilation.Compilation;
import ru.practicum.model.compilation.NewCompilation;
import ru.practicum.service.category.CategoryService;
import ru.practicum.service.compilation.CompilationService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationController {

    private final CompilationService compilationService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        NewCompilation newCompilation = CompilationMapper.toNewCompilation(newCompilationDto);
        return CompilationMapper.toCompilationDto(compilationService.createCompilation(newCompilation));
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilationById(@PathVariable("compId") Long id) {
        if (compilationService.getCompilationById(id) == null) {
            throw new ExploreWithMeNotFoundException("Compilation with Id: " + id + " not found");
        }
        compilationService.deleteCompilationById(id);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@PathVariable("compId") Long id,
                               @Valid @RequestBody UpdateCompilationRequest updateCompilationRequest) {

        return CompilationMapper.toCompilationDto(compilationService.updateCompilation(id,
                CompilationMapper.toUpdateCompilation(updateCompilationRequest)));
    }

















}
