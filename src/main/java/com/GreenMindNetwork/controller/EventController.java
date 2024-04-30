package com.GreenMindNetwork.controller;

import com.GreenMindNetwork.payloads.ApiResponse;
import com.GreenMindNetwork.payloads.EventDto;
import com.GreenMindNetwork.service.EventService;
import com.GreenMindNetwork.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/event")
public class EventController {
    @Autowired
    private EventService eventService;
    @Autowired
    private FileService fileService;
    @Value("${project.image.event}")
    private String path;
    @PostMapping("/{ngoId}")
    public ResponseEntity<EventDto> createEvent(@RequestBody EventDto eventDto, @PathVariable Integer ngoId){
        EventDto event = this.eventService.createEvent(eventDto, ngoId);
        return new ResponseEntity<>(event, HttpStatus.CREATED);
    }
    @PutMapping("/{eventId}")
    public ResponseEntity<EventDto> updateEvent(@RequestBody EventDto eventDto, @PathVariable Integer eventId){
        EventDto event = this.eventService.updateEvent(eventDto, eventId);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }
    @DeleteMapping("/{eventId}")
    public ResponseEntity<ApiResponse> deleteEveent(@PathVariable Integer eventId) throws IOException {
        this.eventService.deleteEvent(eventId);
        ApiResponse apiResponse=new ApiResponse("Event deleted successfully",true);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/ngo/{ngoId}")
    public ResponseEntity<List<EventDto>> getEventByNgo(@PathVariable Integer ngoId){
        List<EventDto> eventByNgo = this.eventService.getEventByNgo(ngoId);
        return ResponseEntity.ok(eventByNgo);

    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDto> getEventById(@PathVariable Integer eventId){
        EventDto eventById = this.eventService.getEventById(eventId);
        return ResponseEntity.ok(eventById);

    }
    @GetMapping("/length")
    public ResponseEntity<Integer> getTotalEvent(){
        Integer totalEvent = this.eventService.getTotalEvent();
        return ResponseEntity.ok(totalEvent);

    } @GetMapping("/length/{ngoId}")
    public ResponseEntity<Integer> getTotalEventByNgo(@PathVariable Integer ngoId){
        Integer totalEventByNgo = this.eventService.getTotalEventByNgo(ngoId);
        return ResponseEntity.ok(totalEventByNgo);

    }
    @GetMapping("/")
    public ResponseEntity<List<EventDto>> getAllEvents(){
        List<EventDto> allEvents = this.eventService.getAllEvents();
        return ResponseEntity.ok(allEvents);
    }
    @PostMapping("/image/{eventId}")
    public ResponseEntity<EventDto> uploadEventImage(@RequestParam("image")MultipartFile image,@PathVariable Integer eventId) throws IOException {
        EventDto event = this.eventService.getEventById(eventId);
        String oldImage = event.getImage();
        String uploadImage = this.fileService.uploadImage(path, image);
        event.setImage(uploadImage);
        EventDto eventDto = this.eventService.updateEvent(event, eventId);
        if(!oldImage.equals("default.jpg")){
            if (!eventDto.getImage().equals(oldImage)) {
                Thread deleteOldImage = new Thread(() -> {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    boolean f = true;
                    while (f) {
                        try {
                            this.fileService.deleteImage(path, oldImage);
                            f=false;
                        } catch (IOException e) {
                            continue;
                        }
                    }
                });
                deleteOldImage.start();
            }
        }
        return  ResponseEntity.ok(eventDto);
    }
    @GetMapping(value = "/image/{imagename}",produces = MediaType.IMAGE_JPEG_VALUE)
    public void ShowImage(@PathVariable String imagename, HttpServletResponse response) throws IOException{
        InputStream image = this.fileService.getResource(path, imagename);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(image, response.getOutputStream());

    }
}
