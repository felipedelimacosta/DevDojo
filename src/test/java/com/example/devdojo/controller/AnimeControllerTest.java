package com.example.devdojo.controller;

import com.example.devdojo.dto.AnimePostDto;
import com.example.devdojo.dto.AnimePutDto;
import com.example.devdojo.model.Anime;
import com.example.devdojo.service.AnimeService;
import com.example.devdojo.util.AnimeCreator;
import com.example.devdojo.util.AnimePostDtoCreator;
import com.example.devdojo.util.AnimePutDtoCreator;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;


@ExtendWith(SpringExtension.class)
@Log4j2
class AnimeControllerTest {

    // Utilizar InjectMocks, para a classe que quero testar
    @InjectMocks
    private AnimeController animeController;

    // Utilizo para todas as classes que estão dentro do "@InjectMocks"
    @Mock
    private AnimeService animeServiceMock;

    @BeforeEach
    void setUp(){
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createAnimeValidAnime()));
        BDDMockito.when(animeServiceMock.listAll(ArgumentMatchers.any()))
                .thenReturn(animePage);

        BDDMockito.when(animeServiceMock.listAllNonPageable())
                .thenReturn(List.of(AnimeCreator.createAnimeValidAnime()));

        BDDMockito.when(animeServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(AnimeCreator.createAnimeValidAnime());

        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createAnimeValidAnime()));

        BDDMockito.when(animeServiceMock.save(ArgumentMatchers.any(AnimePostDto.class)))
                .thenReturn(AnimeCreator.createAnimeValidAnime());

        BDDMockito.doNothing().when(animeServiceMock).replace(ArgumentMatchers.any(AnimePutDto.class));

        BDDMockito.doNothing().when(animeServiceMock).delete(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("List Return List Of Animes Insed PageObject When Successful")
    void list_ReturnListOfAnimesInsedPageObject_WhenSuccessful(){
        String expectedName = AnimeCreator.createAnimeValidAnime().getName();

        Page<Anime> animePage = animeController.list(null).getBody();

        Assertions.assertThat(animePage).isNotNull();

        Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("ListAll Return List Of Animes When Successful")
    void listAll_ReturnListOfAnimes_WhenSuccessful(){
        String expectedName = AnimeCreator.createAnimeValidAnime().getName();

        List<Anime> animeList = animeController.listAll().getBody();

        Assertions.assertThat(animeList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Find By Id Return Anime When Successful")
    void findById_ReturnAnime_WhenSuccessful(){
        Long expectedId = AnimeCreator.createAnimeValidAnime().getId();

        Anime anime = animeController.findById(1).getBody();

        Assertions.assertThat(anime)
                .isNotNull();

        Assertions.assertThat(anime.getId())
                .isNotNull()
                .isEqualTo(expectedId);
    }

    @Test
    @DisplayName("Find By Name Return Anime When Successful")
    void findByName_ReturnAnime_WhenSuccessful(){
        String expectedName = AnimeCreator.createAnimeValidAnime().getName();

        List<Anime> animeList = animeController.findByName("Anime").getBody();

        Assertions.assertThat(animeList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Find By Name Return An Empty List Of Anime When Anime Is Not Found")
    void findByName_ReturnAnEmptyListOfAnime_WhenAnimeIsNotFound(){
        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Anime> animeList = animeController.findByName("Anime").getBody();

        Assertions.assertThat(animeList)
                .isNotNull()
                .isEmpty();
    }


    @Test
    @DisplayName("Save Return Anime When Successful")
    void save_ReturnAnime_WhenSuccessful(){
        Long expectedId = AnimeCreator.createAnimeValidAnime().getId();

        Anime anime = animeController.save(AnimePostDtoCreator.createAnimePostDto()).getBody();

        Assertions.assertThat(anime)
                .isNotNull();

        log.info("Id criado pelo Create Anime Valid Anime: " + expectedId);
        log.info("Id criado pelo AnimeController.save: " + anime.getId());

        Assertions.assertThat(anime.getId())
                .isNotNull()
                .isEqualTo(expectedId);
    }

    @Test
    @DisplayName("Replace Update Anime When Successful")
    void replace_UpdateAnime_WhenSuccessful(){

        Assertions.assertThatCode(() ->
                animeController.replace(AnimePutDtoCreator.createAnimePutDto()).getBody()
        ).doesNotThrowAnyException();

        ResponseEntity<Void> entity = animeController.replace(AnimePutDtoCreator.createAnimePutDto());

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("Delete Removes Anime When Successful")
    void delete_RemovesAnime_WhenSuccessful(){

        Assertions.assertThatCode(() -> animeController.delete(1L))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = animeController.delete(1L);

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}