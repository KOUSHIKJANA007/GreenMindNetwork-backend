package com.GreenMindNetwork.service.impl;

import com.GreenMindNetwork.entities.Ngo;
import com.GreenMindNetwork.entities.NgoSocialImage;
import com.GreenMindNetwork.exception.ResourceNotFoundException;
import com.GreenMindNetwork.payloads.NgoSocialImageDto;
import com.GreenMindNetwork.repositories.NgoRepo;
import com.GreenMindNetwork.repositories.NgoSocialImageRepo;
import com.GreenMindNetwork.service.NgoSocialImageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NgoSocialImageServiceImpl implements NgoSocialImageService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private NgoRepo ngoRepo;
    @Autowired
    private NgoSocialImageRepo ngoSocialImageRepo;
    @Override
    public NgoSocialImageDto postNgoSocialImage(NgoSocialImageDto ngoSocialImageDto, Integer ngoId) {
        Ngo ngo = this.ngoRepo.findById(ngoId).orElseThrow(() -> new ResourceNotFoundException("Ngo", "id", ngoId));
        NgoSocialImage ngoSocialImage = this.modelMapper.map(ngoSocialImageDto, NgoSocialImage.class);
        ngoSocialImage.setImage("default.jpg");
        ngoSocialImage.setCaption(ngoSocialImageDto.getCaption());
        ngoSocialImage.setCreateDate(new Date());
        ngoSocialImage.setNgo(ngo);
        NgoSocialImage socialImage = this.ngoSocialImageRepo.save(ngoSocialImage);
        return this.modelMapper.map(socialImage,NgoSocialImageDto.class);
    }

    @Override
    public NgoSocialImageDto getNgoSocialImageById(Integer socialId) {
        NgoSocialImage ngoSocialImage = this.ngoSocialImageRepo.findById(socialId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", socialId));
        return this.modelMapper.map(ngoSocialImage,NgoSocialImageDto.class);
    }

    @Override
    public List<NgoSocialImageDto> getPostByNgo(Integer ngoId) {
        Ngo ngo = this.ngoRepo.findById(ngoId).orElseThrow(() -> new ResourceNotFoundException("Ngo", "id", ngoId));
        List<NgoSocialImage> byNgo = this.ngoSocialImageRepo.findByNgo(ngo);
        List<NgoSocialImageDto> list = byNgo.stream().map((n) -> this.modelMapper.map(n, NgoSocialImageDto.class)).toList();
        return list;
    }

    @Override
    public NgoSocialImageDto updateNgoSocialImage(NgoSocialImageDto ngoSocialImageDto, Integer socialId) {
        NgoSocialImage ngoSocialImage = this.ngoSocialImageRepo.findById(socialId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", socialId));
        ngoSocialImage.setImage(ngoSocialImageDto.getImage());
        ngoSocialImage.setCaption(ngoSocialImageDto.getCaption());
        NgoSocialImage socialImage = this.ngoSocialImageRepo.save(ngoSocialImage);
        return this.modelMapper.map(socialImage,NgoSocialImageDto.class);
    }


    @Override
    public void deleteNgoSocialImage(Integer socialId) {
        NgoSocialImage ngoSocialImage = this.ngoSocialImageRepo.findById(socialId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", socialId));
        this.ngoSocialImageRepo.delete(ngoSocialImage);
    }
}
