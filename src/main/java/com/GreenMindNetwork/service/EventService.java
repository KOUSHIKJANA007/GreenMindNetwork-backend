package com.GreenMindNetwork.service;

import com.GreenMindNetwork.payloads.EventDto;

import java.util.List;

public interface EventService {
    EventDto createEvent(EventDto eventDto,Integer ngoId);
    EventDto updateEvent(EventDto eventDto,Integer eventId);
    void deleteEvent(Integer eventId);
    List<EventDto> getEventByNgo(Integer ngoId);
    EventDto getEventById(Integer eventId);
    List<EventDto> getAllEvents();
}
