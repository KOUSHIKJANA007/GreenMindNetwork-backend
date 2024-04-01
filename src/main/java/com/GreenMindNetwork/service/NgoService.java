package com.GreenMindNetwork.service;

import com.GreenMindNetwork.payloads.NgoDto;

import java.io.IOException;
import java.util.List;

public interface NgoService {
    NgoDto createNgo(NgoDto ngoDto,Integer userId);
    NgoDto updateNgo(NgoDto ngoDto,Integer ngoId) throws IOException;
    void deleteNgo(Integer ngoId) throws IOException;
    List<NgoDto> getAllNgo();
    NgoDto getNgoById(Integer ngoId);
    NgoDto getNgoByUser(Integer userId);
}
