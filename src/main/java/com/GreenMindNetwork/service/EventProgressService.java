package com.GreenMindNetwork.service;

import com.GreenMindNetwork.entities.EventProgress;
import com.GreenMindNetwork.payloads.EventProgressDto;

import java.util.List;

public interface EventProgressService{
    EventProgressDto createProgress(EventProgressDto progressDto,Integer eventId);
    EventProgressDto updateProgress(EventProgressDto progressDto,Integer progressId);
    void deleteProgress(Integer progressId);
    List<EventProgressDto> getProgress(Integer eventId);
    EventProgressDto getProgressById(Integer progressId);
    EventProgressDto getEventProgressByProgress(Integer progress,Integer eventId);
}
