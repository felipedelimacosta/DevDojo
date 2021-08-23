package com.example.devdojo.util;

import com.example.devdojo.dto.AnimePostDto;

public class AnimePostDtoCreator {

    public static AnimePostDto createAnimePostDto(){
        return AnimePostDto.builder()
                .name(AnimeCreator.createAnimeToBeSaved().getName())
                .build();
    }

}
