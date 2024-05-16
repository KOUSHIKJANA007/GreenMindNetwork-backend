package com.GreenMindNetwork.controller;

import com.GreenMindNetwork.payloads.ApiResponse;
import com.GreenMindNetwork.payloads.EventProgressDto;
import com.GreenMindNetwork.payloads.UserDto;
import com.GreenMindNetwork.payloads.UserEditDto;
import com.GreenMindNetwork.service.EventProgressService;
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

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;

@RestController
@RequestMapping("/progress")
public class EventProgressController {
    @Autowired
    private EventProgressService eventProgressService;
    @Autowired
    private FileService fileService;
    @Value("${project.image.progress}")
    private String path;
    @PostMapping("/{eventId}")
    public ResponseEntity<EventProgressDto> createProgress(@RequestBody EventProgressDto progressDto, @PathVariable Integer eventId){
        EventProgressDto progress = this.eventProgressService.createProgress(progressDto, eventId);
        return new ResponseEntity<>(progress, HttpStatus.CREATED);
    }
    @PutMapping("/{progressId}")
    public ResponseEntity<EventProgressDto> updateProgress(@RequestBody EventProgressDto progressDto, @PathVariable Integer progressId){
        EventProgressDto progress = this.eventProgressService.updateProgress(progressDto, progressId);
        return new ResponseEntity<>(progress, HttpStatus.OK);
    }
    @DeleteMapping("/{progressId}")
    public ResponseEntity<ApiResponse> deleteProgress(@PathVariable Integer progressId){
        this.eventProgressService.deleteProgress(progressId);
        return new ResponseEntity<>(new ApiResponse("process deleted",true), HttpStatus.OK);
    }
    @GetMapping("/{eventId}")
    public ResponseEntity<List<EventProgressDto>> getProgressByEvent(@PathVariable Integer eventId){
        List<EventProgressDto> progress = this.eventProgressService.getProgress(eventId);
        return ResponseEntity.ok(progress);
    }

    @GetMapping("/event-progress/{progress}/event/{eventId}")
    public ResponseEntity<EventProgressDto> getProgressByProgress(@PathVariable Integer progress,@PathVariable Integer eventId){
        EventProgressDto eventProgressByProgress = this.eventProgressService.getEventProgressByProgress(progress,eventId);
        return ResponseEntity.ok(eventProgressByProgress);
    }

    @PostMapping(value = "/image/upload/{progressId}")
    public ResponseEntity<EventProgressDto> uploadImage(
            @PathVariable Integer progressId,
            @RequestParam("image") MultipartFile image) throws IOException, InterruptedException {

        EventProgressDto progressById = this.eventProgressService.getProgressById(progressId);
        String oldImage = progressById.getImageName();

        String uploadImage=this.fileService.uploadImage(path, image);
        progressById.setImageName(uploadImage);
        EventProgressDto eventProgressDto = this.eventProgressService.updateProgress(progressById, progressId);
        if(!oldImage.equals("default.png")){
            if(!eventProgressDto.getImageName().equals(oldImage)){
                Thread deleteOldImage=new Thread(()->{
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    boolean f=true;
                    while (f){
                        try {
                            this.fileService.deleteImage(path,oldImage);
                            f=false;
                        } catch (IOException e) {
                            continue;
                        }
                    }
                });
                deleteOldImage.start();
            }
        }
        return new ResponseEntity<>(eventProgressDto,HttpStatus.OK);
    }

    @GetMapping(value = "/image/{imagename}",produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(
            @PathVariable("imagename") String imagename,
            HttpServletResponse response) throws IOException{
        InputStream resource = this.fileService.getResource(path, imagename);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }
}
