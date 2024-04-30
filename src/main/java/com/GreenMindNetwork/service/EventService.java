package com.GreenMindNetwork.service;

import com.GreenMindNetwork.payloads.EventDto;

import java.io.IOException;
import java.util.List;

public interface EventService {
    EventDto createEvent(EventDto eventDto,Integer ngoId);
    EventDto updateEvent(EventDto eventDto,Integer eventId);
    void deleteEvent(Integer eventId) throws IOException;
    List<EventDto> getEventByNgo(Integer ngoId);
    EventDto getEventById(Integer eventId);
    List<EventDto> getAllEvents();
    Integer getTotalEvent();
    Integer getTotalEventByNgo(Integer ngoId);
}
