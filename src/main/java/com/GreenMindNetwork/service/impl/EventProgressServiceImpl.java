package com.GreenMindNetwork.service.impl;

import com.GreenMindNetwork.entities.Event;
import com.GreenMindNetwork.entities.EventProgress;
import com.GreenMindNetwork.exception.ResourceNotFoundException;
import com.GreenMindNetwork.payloads.EventProgressDto;
import com.GreenMindNetwork.repositories.EventProgressRepo;
import com.GreenMindNetwork.repositories.EventRepo;
import com.GreenMindNetwork.service.EventProgressService;
import com.GreenMindNetwork.service.FileService;
import org.hibernate.NonUniqueResultException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
@Service
public class EventProgressServiceImpl implements EventProgressService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private EventProgressRepo eventProgressRepo;
    @Autowired
    private EventRepo eventRepo;
    @Autowired
    private FileService fileService;
    @Value("${project.image.progress}")
    private String path;
    @Override
    public EventProgressDto createProgress(EventProgressDto progressDto, Integer eventId) {
        Event event = this.eventRepo.findById(eventId).orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));
        EventProgress eventProgress = this.modelMapper.map(progressDto, EventProgress.class);
        eventProgress.setProgress(progressDto.getProgress());
        eventProgress.setCaption(progressDto.getCaption());
        eventProgress.setImageName("default.jpg");
        eventProgress.setDescription(progressDto.getDescription());
        eventProgress.setTotalCost(progressDto.getTotalCost());
        eventProgress.setEvent(event);
        EventProgress savedProgress = this.eventProgressRepo.save(eventProgress);
        return this.modelMapper.map(savedProgress,EventProgressDto.class);
    }

    @Override
    public EventProgressDto updateProgress(EventProgressDto progressDto, Integer progressId) {
        EventProgress eventProgress = this.eventProgressRepo.findById(progressId).orElseThrow(() -> new ResourceNotFoundException("EventProgress", "id", progressId));
        eventProgress.setCaption(progressDto.getCaption());
        eventProgress.setImageName(progressDto.getImageName());
        eventProgress.setDescription(progressDto.getDescription());
        eventProgress.setTotalCost(progressDto.getTotalCost());
        EventProgress savedProgress = this.eventProgressRepo.save(eventProgress);
        return this.modelMapper.map(savedProgress,EventProgressDto.class);
    }

    @Override
    public void deleteProgress(Integer progressId) {
        EventProgress eventProgress = this.eventProgressRepo.findById(progressId).orElseThrow(() -> new ResourceNotFoundException("EventProgress", "id", progressId));
        if(!eventProgress.getImageName().equals("default.jpg")){
            Thread deleteImage=new Thread(()->{
                boolean f=true;
                while (f) {
                    try {
                        this.fileService.deleteImage(path, eventProgress.getImageName());
                        f=false;
                    } catch (IOException e) {
                       continue;
                    }
                }
            });
            deleteImage.start();
        }
        this.eventProgressRepo.delete(eventProgress);
    }

    @Override
    public List<EventProgressDto> getProgress(Integer eventId) {
        Event event = this.eventRepo.findById(eventId).orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));
        List<EventProgress> byEvent = this.eventProgressRepo.findByEvent(event);
        return byEvent.stream().map((e) -> this.modelMapper.map(e, EventProgressDto.class)).toList();

    }

    @Override
    public EventProgressDto getProgressById(Integer progressId) {
        EventProgress eventProgress = this.eventProgressRepo.findById(progressId).orElseThrow(() -> new ResourceNotFoundException("EventProgress", "id", progressId));
        return this.modelMapper.map(eventProgress,EventProgressDto.class);
    }

    @Override
    public EventProgressDto getEventProgressByProgress(Integer progress,Integer eventId) throws NonUniqueResultException {
        EventProgress byProgress = this.eventProgressRepo.findByProgressAndEvent(progress,eventId);
        return this.modelMapper.map(byProgress,EventProgressDto.class);
    }
}
