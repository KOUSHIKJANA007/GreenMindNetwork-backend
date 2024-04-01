package com.GreenMindNetwork.service;

import com.GreenMindNetwork.payloads.NgoSocialImageDto;

import java.io.IOException;
import java.util.List;

public interface NgoSocialImageService {
    NgoSocialImageDto postNgoSocialImage(NgoSocialImageDto ngoSocialImageDto,Integer ngoId);
    NgoSocialImageDto getNgoSocialImageById(Integer socialId);
    List<NgoSocialImageDto> getPostByNgo(Integer ngoId);
    NgoSocialImageDto updateNgoSocialImage(NgoSocialImageDto ngoSocialImageDto,Integer socialId) throws IOException;
    void deleteNgoSocialImage(Integer socialId) throws IOException;
}
