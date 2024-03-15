package com.GreenMindNetwork.service;

import com.GreenMindNetwork.payloads.NgoDto;

import java.util.List;

public interface NgoService {
    NgoDto createNgo(NgoDto ngoDto,Integer userId);
    NgoDto updateNgo(NgoDto ngoDto,Integer ngoId);
    void deleteNgo(Integer ngoId);
    List<NgoDto> getAllNgo();
    NgoDto getNgoById(Integer ngoId);
    NgoDto getNgoByUser(Integer userId);
}
