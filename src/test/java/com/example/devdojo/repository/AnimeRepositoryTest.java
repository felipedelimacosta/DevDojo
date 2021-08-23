package com.example.devdojo.repository;

import com.example.devdojo.model.Anime;
import com.example.devdojo.util.AnimeCreator;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@DisplayName("Tests for Anime Repository")
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
@Log4j2
class AnimeRepositoryTest {

    @Autowired
    private AnimeRepository animeRepository;

    @Test
    @DisplayName("Save persists anime when Sucessful")
    void save_PersistAnime_WhenSuccessful(){
        Anime anime = AnimeCreator.createAnimeToBeSaved();
        Anime saveAnime = this.animeRepository.save(anime);

        Assertions.assertThat(saveAnime).isNotNull();

        Assertions.assertThat(saveAnime.getId()).isNotNull();

        Assertions.assertThat(saveAnime.getName()).isEqualTo(anime.getName());

    }

    @Test
    @DisplayName("Save persists anime when Sucessful")
    void save_UpdateAnime_WhenSuccessful(){
        Anime anime = AnimeCreator.createAnimeToBeSaved();
        Anime saveAnime = this.animeRepository.save(anime);

        saveAnime.setName("Overlord");

        Anime animeUpdated = this.animeRepository.save(saveAnime);

        //log.info(animeUpdated.getName());
        Assertions.assertThat(animeUpdated).isNotNull();

        Assertions.assertThat(animeUpdated.getId()).isNotNull();

        Assertions.assertThat(animeUpdated.getName()).isEqualTo(saveAnime.getName());

    }

    @Test
    @DisplayName("Delete removes anime when Sucessful")
    void delete_RemoveAnime_WhenSuccessful(){
        Anime anime = AnimeCreator.createAnimeToBeSaved();
        Anime saveAnime = this.animeRepository.save(anime);

        this.animeRepository.delete(saveAnime);

        Optional<Anime> animeOptional = this.animeRepository.findById(saveAnime.getId());

        Assertions.assertThat(animeOptional).isEmpty();
    }

    @Test
    @DisplayName("Find By Name returns list of anime when Sucessful")
    void findByName_ReturnsListOfAnime_WhenSuccessful(){
        Anime anime = AnimeCreator.createAnimeToBeSaved();
        Anime saveAnime = this.animeRepository.save(anime);

        String name = saveAnime.getName();

        List<Anime> animes = this.animeRepository.findByName(name);

        Assertions.assertThat(animes).isNotEmpty();

        Assertions.assertThat(animes).contains(saveAnime);
    }

    @Test
    @DisplayName("Find By Name returns empty list when anime is not found")
    void findByName_ReturnsEmptyList_WhenAnimeIsNotFound(){
        List<Anime> animes = this.animeRepository.findByName("anime");

        Assertions.assertThat(animes).isEmpty();
    }

    @Test
    @DisplayName("Save throw ConstranintValidationException when name is empty")
    void save_ThrowConstranintValidationException_WhenNameIsEmpty(){
        Anime anime = new Anime();
//        Assertions.assertThatThrownBy(() -> this.animeRepository.save(anime))
//                .isInstanceOf(ConstraintViolationException.class);

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> this.animeRepository.save(anime))
                .withMessageContaining("The anime name cannot be empty");
    }

}