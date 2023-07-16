package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.request.ParticipationRequest;
import ru.practicum.model.request.RequestStatus;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {

    ParticipationRequest findAllByEventIdAndRequesterId(Long eventId, Long userId);

    List<ParticipationRequest> findByRequesterId(Long requesterId);

    List<ParticipationRequest> findByEventId(Long EventId);

    List<ParticipationRequest> findByEventIdAndStatus(Long EventId, RequestStatus status);
}