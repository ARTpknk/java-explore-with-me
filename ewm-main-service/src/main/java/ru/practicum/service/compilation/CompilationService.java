package ru.practicum.service.compilation;

import ru.practicum.model.compilation.Compilation;
import ru.practicum.model.compilation.NewCompilation;

import java.util.List;

public interface CompilationService {
    Compilation createCompilation(NewCompilation newCompilation);

    Compilation getCompilationById(Long id);

    void deleteCompilationById(Long id);

    Compilation updateCompilation(Long id, NewCompilation updateCompilation);

    List<Compilation> getCompilations(int from, int size, Boolean pinned);
}