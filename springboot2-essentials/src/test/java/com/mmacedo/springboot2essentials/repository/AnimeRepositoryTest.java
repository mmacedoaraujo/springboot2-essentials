package com.mmacedo.springboot2essentials.repository;

import com.mmacedo.springboot2essentials.domain.Anime;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@DisplayName("Tests for anime repository")
@Log4j2
class AnimeRepositoryTest {

    //in test cases is okay to use @Autowired
    @Autowired
    private AnimeRepository animeRepository;

    @Test
    @DisplayName("Save creates anime when succesful")
    void save_PersistAnime_WhenSuccessful() {
        Anime animeToBeSaved = createAnime();
        Anime savedAnime = this.animeRepository.save(createAnime());
        Assertions.assertThat(savedAnime).isNotNull();
        Assertions.assertThat(savedAnime.getId()).isNotNull();
        Assertions.assertThat(savedAnime.getName()).isEqualTo(animeToBeSaved.getName());
    }

    @Test
    @DisplayName("Save updates anime when successful")
    void save_UpdatesAnime_WhenSuccesful() {
        Anime animeToBeUpdated = createAnime();

        Anime savedAnime = this.animeRepository.save(animeToBeUpdated);

        savedAnime.setName("Overlord");

        Anime updatedAnime = this.animeRepository.save(animeToBeUpdated);

        Assertions.assertThat(updatedAnime).isNotNull();

        Assertions.assertThat(updatedAnime.getId()).isNotNull();

        Assertions.assertThat(updatedAnime).isEqualTo(savedAnime);
    }

    @Test
    @DisplayName("Delete removes when successful")
    void delete_RemovesAnime_WhenSuccessful() {
        Anime animeToBeDeleted = createAnime();

        Anime savedAnime = this.animeRepository.save(animeToBeDeleted);

        this.animeRepository.delete(savedAnime);

        Optional<Anime> animeOptional = this.animeRepository.findById(savedAnime.getId());

        Assertions.assertThat(animeOptional).isEmpty();
    }
    
    @Test
    @DisplayName("Find by name returns list when successful")
    void finds_AnimeByNameReturnsList_WhenSuccessful() {
        Anime animeToBeSaved = createAnime();
        
        Anime animeSaved = this.animeRepository.save(animeToBeSaved);

        String name = animeSaved.getName();
        List<Anime> animes = this.animeRepository.findByName(name);

        Assertions.assertThat(animes).isNotEmpty().contains(animeSaved);
    }

    @Test
    @DisplayName("Find by name returns list when successful")
    void findByName_ReturnsEmptyList_WhenAnimeNotFound() {
        List<Anime> animes = this.animeRepository.findByName("dsadjs");

        Assertions.assertThat(animes).isEmpty();
    }
    private Anime createAnime() {
        return Anime.builder()
                .name("Hajime no Ippo")
                .build();
    }
}