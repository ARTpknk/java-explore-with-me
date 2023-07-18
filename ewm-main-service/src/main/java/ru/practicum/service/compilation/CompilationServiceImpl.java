package ru.practicum.service.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exception.ExploreWithMeNotFoundException;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.model.compilation.Compilation;
import ru.practicum.model.compilation.NewCompilation;
import ru.practicum.model.compilation.UpdateCompilation;
import ru.practicum.model.event.Event;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.service.event.EventService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository repository;
    private final EventService eventService;

    @Override
    public Compilation createCompilation(NewCompilation newCompilation) {
        List<Event> events = new ArrayList<>();
        if (newCompilation.getEvents() != null) {
            events = eventService.getEventsByIds(newCompilation.getEvents());
        }
        Compilation compilation = Compilation.builder()
                .pinned(newCompilation.getPinned())
                .title(newCompilation.getTitle())
                .events(new HashSet<>(events))
                .build();
        return repository.save(compilation);
    }

    @Override
    public Compilation getCompilationById(Long id) {
        if (repository.findById(id).isPresent()) {
            return repository.findById(id).get();
        } else {
            throw new ExploreWithMeNotFoundException("Compilation with Id: " + id + " not found");
        }
    }

    @Override
    public void deleteCompilationById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Compilation updateCompilation(Long id, UpdateCompilation updateCompilation) {
        Compilation compilation = getCompilationById(id);
        if (updateCompilation.getEvents() != null && !updateCompilation.getEvents().isEmpty()) {
            List<Event> events = new ArrayList<>();
            events = eventService.getEventsByIds(updateCompilation.getEvents());
            compilation.setEvents(new HashSet<>(events));
        }
        return repository.save(CompilationMapper.updateCompilation(compilation, updateCompilation));
    }

    @Override
    public List<Compilation> getCompilations(int from, int size, Boolean pinned) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        if (pinned == null) {
            return repository.findAll(pageRequest).getContent();
        } else {
            return repository.findAllByPinned(pinned, pageRequest);
        }
    }
}