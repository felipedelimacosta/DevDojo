package com.example.devdojo.service;


import com.example.devdojo.dto.AnimePostDto;
import com.example.devdojo.dto.AnimePutDto;
import com.example.devdojo.exception.BadRequestException;
import com.example.devdojo.mapper.AnimeMapper;
import com.example.devdojo.model.Anime;
import com.example.devdojo.repository.AnimeRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {

    @Autowired
    private  AnimeRepository animeRepository;

    public Page<Anime> listAll(Pageable pageable) {
        return animeRepository.findAll(pageable);
    }

    @Transactional
    public Anime save(AnimePostDto animePostRequestBody) {
        return animeRepository.save(AnimeMapper.INSTANCE.toAnime(animePostRequestBody));
    }

    @SneakyThrows
    public Anime findByIdOrThrowBadRequestException(long id) {
        return animeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Anime not found"));
    }

    @SneakyThrows
    public void replace(AnimePutDto animePutRequestBody) {
        Anime savedAnime = findByIdOrThrowBadRequestException(animePutRequestBody.getId());
        Anime anime = AnimeMapper.INSTANCE.toAnime(animePutRequestBody);
        anime.setId(savedAnime.getId());
        animeRepository.save(anime);
    }

    public List<Anime> listAllNonPageable() {
        return animeRepository.findAll();
    }
    public List<Anime> findByName(String name){
        return animeRepository.findByName(name);
    }

    public void delete(long id) {
        animeRepository.delete(findByIdOrThrowBadRequestException(id));
    }





}