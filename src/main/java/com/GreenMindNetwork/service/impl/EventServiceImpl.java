package com.GreenMindNetwork.service.impl;

import com.GreenMindNetwork.entities.Event;
import com.GreenMindNetwork.entities.Ngo;
import com.GreenMindNetwork.exception.ResourceNotFoundException;
import com.GreenMindNetwork.payloads.EventDto;
import com.GreenMindNetwork.repositories.EventRepo;
import com.GreenMindNetwork.repositories.NgoRepo;
import com.GreenMindNetwork.service.EventService;
import com.GreenMindNetwork.service.FileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private EventRepo eventRepo;
    @Autowired
    private NgoRepo ngoRepo;
    @Autowired
    private FileService fileService;
    @Value("${project.image.event}")
    private String path;
    @Override
    public EventDto createEvent(EventDto eventDto, Integer ngoId) {
        Ngo ngo = this.ngoRepo.findById(ngoId).orElseThrow(() -> new ResourceNotFoundException("Ngo", "id", ngoId));
        Event event = this.modelMapper.map(eventDto, Event.class);
        event.setTitle(eventDto.getTitle());
        event.setCreateDate(new Date());
        event.setDescription(eventDto.getDescription());
        event.setTargetAmount(eventDto.getTargetAmount());
        event.setImage("default.jpg");
        event.setNgo(ngo);
        Event savedEvent = this.eventRepo.save(event);
        return this.modelMapper.map(savedEvent,EventDto.class);
    }

    @Override
    public EventDto updateEvent(EventDto eventDto, Integer eventId) {
        Event event = this.eventRepo.findById(eventId).orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));
        event.setTitle(eventDto.getTitle());
        event.setDescription(eventDto.getDescription());
        event.setTargetAmount(eventDto.getTargetAmount());
        event.setImage(eventDto.getImage());
        Event savedEvent = this.eventRepo.save(event);
        return this.modelMapper.map(savedEvent,EventDto.class);
    }

    @Override
    public void deleteEvent(Integer eventId) throws IOException {
        Event event = this.eventRepo.findById(eventId).orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));
        boolean f = true;
        while (f) {
            try {
                this.fileService.deleteImage(path, event.getImage());
                f=false;
            } catch (IOException e) {
                continue;
            }
        }
        this.eventRepo.delete(event);
    }

    @Override
    public List<EventDto> getEventByNgo(Integer ngoId) {
        Ngo ngo = this.ngoRepo.findById(ngoId).orElseThrow(() -> new ResourceNotFoundException("Ngo", "id", ngoId));
        List<Event> byNgo = this.eventRepo.findByNgo(ngo);
        List<EventDto> eventDtoList = byNgo.stream().map((eve) -> this.modelMapper.map(eve, EventDto.class)).toList();
        return eventDtoList;
    }

    @Override
    public EventDto getEventById(Integer eventId) {
       Event byId = this.eventRepo.findById(eventId).orElseThrow(()->new ResourceNotFoundException("Event","id",eventId));
        return this.modelMapper.map(byId,EventDto.class);
    }

    @Override
    public List<EventDto> getAllEvents() {
        List<Event> events = this.eventRepo.findAll();
        List<EventDto> list = events.stream().map((e) -> this.modelMapper.map(e, EventDto.class)).toList();

        return list;
    }

    @Override
    public Integer getTotalEvent() {
        List<Event> allEvents = this.eventRepo.findAll();
        return allEvents.size();
    }

    @Override
    public Integer getTotalEventByNgo(Integer ngoId) {
        Ngo ngo = this.ngoRepo.findById(ngoId).orElseThrow(() -> new ResourceNotFoundException("Ngo", "id", ngoId));
        List<Event> byNgo = this.eventRepo.findByNgo(ngo);
        return byNgo.size();
    }
}
