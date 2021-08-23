package com.example.devdojo.service;

import com.example.devdojo.exception.BadRequestException;
import com.example.devdojo.model.Anime;
import com.example.devdojo.repository.AnimeRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@Log4j2
class AnimeServiceTest {


    // Utilizar InjectMocks, para a classe que quero testar
    @InjectMocks
    private AnimeService animeService;

    // Utilizo para todas as classes que estão dentro do "@InjectMocks"
    @Mock
    private AnimeRepository animeRepositoryMock;

    @BeforeEach
    void setUp(){
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createAnimeValidAnime()));
        BDDMockito.when(animeRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(animePage);

        BDDMockito.when(animeRepositoryMock.findAll())
                .thenReturn(List.of(AnimeCreator.createAnimeValidAnime()));

        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(AnimeCreator.createAnimeValidAnime()));

        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createAnimeValidAnime()));

        BDDMockito.when(animeRepositoryMock.save(ArgumentMatchers.any(Anime.class)))
                .thenReturn(AnimeCreator.createAnimeValidAnime());

        BDDMockito.doNothing().when(animeRepositoryMock).delete(ArgumentMatchers.any(Anime.class));
    }

    @Test
    @DisplayName("List Return List Of Animes Insed PageObject When Successful")
    void list_ReturnListOfAnimesInsedPageObject_WhenSuccessful(){
        String expectedName = AnimeCreator.createAnimeValidAnime().getName();

        Page<Anime> animePage = animeService.listAll(PageRequest.of(1,1));

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

        List<Anime> animeList = animeService.listAllNonPageable();

        Assertions.assertThat(animeList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Find By Id Return Anime When Successful")
    void findByIdOrThrowBadRequestException_ReturnAnime_WhenSuccessful(){
        Long expectedId = AnimeCreator.createAnimeValidAnime().getId();

        Anime anime = animeService.findByIdOrThrowBadRequestException(1L);

        Assertions.assertThat(anime)
                .isNotNull();

        Assertions.assertThat(anime.getId())
                .isNotNull()
                .isEqualTo(expectedId);
    }

    @Test
    @DisplayName("find By Id Or Throw Bad Request Exception Throws BadRequestException When Anime Is Not Found")
    void findByIdOrThrowBadRequestException_ThrowBadRequestException_WhenAnimeIsNotFound(){
        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> this.animeService.findByIdOrThrowBadRequestException(1L));
    }

    @Test
    @DisplayName("Find By Name Return Anime When Successful")
    void findByName_ReturnAnime_WhenSuccessful(){
        String expectedName = AnimeCreator.createAnimeValidAnime().getName();

        List<Anime> animeList = animeService.findByName("Anime");

        Assertions.assertThat(animeList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Find By Name Return An Empty List Of Anime When Anime Is Not Found")
    void findByName_ReturnAnEmptyListOfAnime_WhenAnimeIsNotFound(){
        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Anime> animeList = animeService.findByName("Anime");

        Assertions.assertThat(animeList)
                .isNotNull()
                .isEmpty();
    }


    @Test
    @DisplayName("Save Return Anime When Successful")
    void save_ReturnAnime_WhenSuccessful(){
        Long expectedId = AnimeCreator.createAnimeValidAnime().getId();

        Anime anime = animeService.save(AnimePostDtoCreator.createAnimePostDto());

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
                animeService.replace(AnimePutDtoCreator.createAnimePutDto())
        ).doesNotThrowAnyException();

    }

    @Test
    @DisplayName("Delete Removes Anime When Successful")
    void delete_RemovesAnime_WhenSuccessful(){

        Assertions.assertThatCode(() -> animeService.delete(1L))
                .doesNotThrowAnyException();

    }
}