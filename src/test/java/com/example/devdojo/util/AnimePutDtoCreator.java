package com.example.devdojo.util;

import com.example.devdojo.dto.AnimePutDto;

public class AnimePutDtoCreator {

    public static AnimePutDto createAnimePutDto(){
        return AnimePutDto.builder()
                .id(AnimeCreator.createAnimeValidUpdatedAnime().getId())
                .name(AnimeCreator.createAnimeValidUpdatedAnime().getName())
                .build();
    }

}
