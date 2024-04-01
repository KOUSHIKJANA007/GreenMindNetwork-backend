package com.GreenMindNetwork.service.impl;

import com.GreenMindNetwork.config.AppConstants;
import com.GreenMindNetwork.entities.Ngo;
import com.GreenMindNetwork.entities.Role;
import com.GreenMindNetwork.entities.User;
import com.GreenMindNetwork.exception.ResourceNotFoundException;
import com.GreenMindNetwork.payloads.ApiResponse;
import com.GreenMindNetwork.payloads.NgoDto;
import com.GreenMindNetwork.repositories.NgoRepo;
import com.GreenMindNetwork.repositories.RoleRepo;
import com.GreenMindNetwork.repositories.UserRepo;
import com.GreenMindNetwork.service.FileService;
import com.GreenMindNetwork.service.NgoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NgoServiceImpl implements NgoService{
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private NgoRepo ngoRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepo userRepo;

    @Override
    public NgoDto createNgo(NgoDto ngoDto,Integer userId) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Ngo ngo = this.modelMapper.map(ngoDto, Ngo.class);
        ngo.setName(ngoDto.getName());
        ngo.setEmail(ngoDto.getEmail());
        ngo.setAddress(ngoDto.getAddress());
        ngo.setNameOfHead(user.getFname()+" "+user.getLname());
        ngo.setEstablishedDate(ngoDto.getEstablishedDate());
        ngo.setSlogan(ngoDto.getSlogan());
        ngo.setDescription(ngoDto.getDescription());
        ngo.setMobile(ngoDto.getMobile());
        ngo.setIdentityOfHead("default.jpg");
        ngo.setImageOfTax("default.jpg");
        ngo.setRegisterImage("default.jpg");
        ngo.setLogo("default.jpg");
        ngo.setUser(user);
        Role role = this.roleRepo.findById(AppConstants.NGO_USER).get();
        user.getRoles().add(role);
        Ngo savedNgo = this.ngoRepo.save(ngo);
        return this.modelMapper.map(savedNgo,NgoDto.class);
    }

    @Override
    public NgoDto updateNgo(NgoDto ngoDto, Integer ngoId) throws IOException {
        Ngo ngo = this.ngoRepo.findById(ngoId).orElseThrow(() -> new ResourceNotFoundException("NGO", "id", ngoId));
        ngo.setName(ngoDto.getName());
        ngo.setEmail(ngoDto.getEmail());
        ngo.setAddress(ngoDto.getAddress());
        ngo.setNameOfHead(ngoDto.getNameOfHead());
        ngo.setEstablishedDate(ngoDto.getEstablishedDate());
        ngo.setSlogan(ngoDto.getSlogan());
        ngo.setDescription(ngoDto.getDescription());
        ngo.setMobile(ngoDto.getMobile());
        ngo.setIdentityOfHead(ngoDto.getIdentityOfHead());
        ngo.setImageOfTax(ngoDto.getImageOfTax());
        ngo.setRegisterImage(ngoDto.getRegisterImage());
        ngo.setLogo(ngoDto.getLogo());
        Ngo updatedNgo = this.ngoRepo.save(ngo);
        return this.modelMapper.map(updatedNgo,NgoDto.class);
    }

    @Override
    public void deleteNgo(Integer ngoId) throws IOException {
        Ngo ngo = this.ngoRepo.findById(ngoId).orElseThrow(() -> new ResourceNotFoundException("NGO", "id", ngoId));

        this.ngoRepo.delete(ngo);
    }

    @Override
    public List<NgoDto> getAllNgo() {
        List<Ngo> ngos = this.ngoRepo.findAll();
        List<NgoDto> collectedNgos = ngos.stream().map((ngo) -> this.modelMapper.map(ngo, NgoDto.class)).collect(Collectors.toList());
        return collectedNgos;
    }

    @Override
    public NgoDto getNgoById(Integer ngoId) {
        Ngo ngo = this.ngoRepo.findById(ngoId).orElseThrow(() -> new ResourceNotFoundException("NGO", "id", ngoId));

        return this.modelMapper.map(ngo,NgoDto.class);
    }

    @Override
    public NgoDto getNgoByUser(Integer userId) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user", "id", userId));
        Ngo byUser = this.ngoRepo.findByUser(user);
        if(byUser==null){
            ApiResponse apiResponse=new ApiResponse("NGO not found",false);
            return this.modelMapper.map(apiResponse,NgoDto.class);
        }
        return this.modelMapper.map(byUser,NgoDto.class);
    }
}
