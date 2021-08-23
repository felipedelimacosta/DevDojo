package com.example.devdojo.mapper;

import com.example.devdojo.dto.AnimePostDto;
import com.example.devdojo.dto.AnimePutDto;
import com.example.devdojo.model.Anime;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class AnimeMapper {

    public static final AnimeMapper INSTANCE = Mappers.getMapper(AnimeMapper.class);

    public abstract Anime toAnime(AnimePostDto animePostDto);

    public abstract Anime toAnime(AnimePutDto animePostDto);
}
