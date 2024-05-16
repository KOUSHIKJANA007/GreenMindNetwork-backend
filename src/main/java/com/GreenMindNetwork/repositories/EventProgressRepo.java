package com.GreenMindNetwork.repositories;

import com.GreenMindNetwork.entities.Event;
import com.GreenMindNetwork.entities.EventProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventProgressRepo extends JpaRepository<EventProgress,Integer> {
    List<EventProgress> findByEvent(Event event);
    @Query("select p from EventProgress p where p.progress=:progress and p.event.id=:eventId")
    EventProgress findByProgressAndEvent(@Param("progress") Integer progress,@Param("eventId") Integer eventId);
}
