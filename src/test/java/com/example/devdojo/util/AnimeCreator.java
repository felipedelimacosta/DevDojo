package com.example.devdojo.util;

import com.example.devdojo.model.Anime;

public class AnimeCreator {

    public static Anime createAnimeToBeSaved(){
        return Anime.builder()
                .name("Full Metal")
                .build();
    }

    public static Anime createAnimeValidAnime(){
        return Anime.builder()
                .name("Full Metal")
                .id(1L)
                .build();
    }

    public static Anime createAnimeValidUpdatedAnime(){
        return Anime.builder()
                .name("Full Metal BrotherHood")
                .id(1L)
                .build();
    }

}
