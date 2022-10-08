package com.mmacedo.springboot2essentials.controller;

import com.mmacedo.springboot2essentials.domain.Anime;
import com.mmacedo.springboot2essentials.requests.AnimePostRequestBody;
import com.mmacedo.springboot2essentials.requests.AnimePutRequestBody;
import com.mmacedo.springboot2essentials.service.AnimeService;
import com.mmacedo.springboot2essentials.util.AnimeCreator;
import com.mmacedo.springboot2essentials.util.AnimePostRequestBodyCreator;
import com.mmacedo.springboot2essentials.util.AnimePutRequestBodyCreator;
import com.mmacedo.springboot2essentials.util.DateUtil;
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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class AnimeControllerTest {

    @InjectMocks
    private AnimeController animeController;
    @Mock
    private AnimeService animeService;
    @Mock
    private DateUtil dateUtil;

    @BeforeEach
    void setUp() {
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
        BDDMockito.when(animeService.listAll(ArgumentMatchers.any()))
                .thenReturn(animePage);

        BDDMockito.when(dateUtil.formatLocalDateTimeToDatabaseStyle(ArgumentMatchers.any()))
                .thenReturn(String.valueOf(LocalDateTime.now()));

        BDDMockito.when(animeService.listAllNonPageable())
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeService.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(AnimeCreator.createValidAnime());

        BDDMockito.when(animeService.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeService.save(ArgumentMatchers.any(AnimePostRequestBody.class)))
                .thenReturn(AnimeCreator.createValidAnime());

        BDDMockito.doNothing().when(animeService)
                .replace(ArgumentMatchers.any(AnimePutRequestBody.class));

        BDDMockito.doNothing().when(animeService)
                .delete(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("List returns list of anime inside page object when successful")
    void list_ReturnsListOfAnimeInsidePageObject_WhenSuccessful() {
        Anime validAnime = AnimeCreator.createValidAnime();
        Page<Anime> animePage = animeController.list(null).getBody();

        Assertions.assertThat(animePage)
                .isNotNull();

        Assertions.assertThat(animePage.toList())
                .isNotNull()
                .hasSize(1);

        Assertions.assertThat(animePage.toList().get(0).getName())
                .isEqualTo(validAnime.getName());

        Assertions.assertThat(animePage.toList().get(0).getId())
                .isEqualTo(validAnime.getId());
    }

    @Test
    @DisplayName("Listall return list of anime when successful")
    void listAll_ReturnsListOfAnime_WhenSuccessful() {
        Anime testAnime = AnimeCreator.createValidAnime();

        List<Anime> animes = animeController.listAll().getBody();

        Assertions.assertThat(animes)
                .isNotEmpty()
                .isNotNull()
                .hasSize(1);
        Assertions.assertThat(animes.get(0).getName())
                .isEqualTo(testAnime.getName());
    }

    @Test
    @DisplayName("FindById returns a valid anime when successful")
    void findById_ReturnsAnValidAnime_WhenSuccessful() {
        Anime comparisonAnime = AnimeCreator.createValidAnime();

        Anime foundAnime = animeController.findById(2L).getBody();

        Assertions.assertThat(foundAnime)
                .isNotNull()
                .isInstanceOf(Anime.class);

        Assertions.assertThat(foundAnime.getId())
                .isNotNull()
                .isEqualTo(comparisonAnime.getId());
    }

    @Test
    @DisplayName("findByName returns a list of animes when successful")
    void findByName_ReturnsListOfAnime_WhenSuccessful() {
        Anime comparisonAnime = AnimeCreator.createValidAnime();

        List<Anime> animes = animeController.findByName("a").getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animes.get(0).getId())
                .isEqualTo(comparisonAnime.getId());

    }

    @Test
    @DisplayName("findByName returns an empty list of anime when anime is not found")
    void findByName_ReturnsEmptyListOfAnime_WhenAnimeIsNotFound() {
        BDDMockito.when(animeService.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Anime> anime = animeController.findByName("anime").getBody();

        Assertions.assertThat(anime)
                .isNotNull()
                .isEmpty();


    }

    @Test
    @DisplayName("save returns anime when successful")
    void save_ReturnAnime_WhenSuccessful() {

        Anime anime = animeController.save(AnimePostRequestBodyCreator.createAnimeToBeSaved()).getBody();

        Assertions.assertThat(anime)
                .isNotNull();

        Assertions.assertThat(anime.getId())
                .isEqualTo(AnimeCreator.createValidAnime().getId());

        Assertions.assertThat(anime.getName())
                .isEqualTo(AnimeCreator.createValidAnime().getName());

    }

    @Test
    @DisplayName("replace updates anime when successful")
    void replace_UpdatesAnime_WhenSuccessful() {

        Assertions.assertThatCode(() -> animeController.replace(AnimePutRequestBodyCreator.createAnimeToBeSaved()))
                .doesNotThrowAnyException();

        ResponseEntity<Void> replaceAnime = animeController.replace(AnimePutRequestBodyCreator.createAnimeToBeSaved());

        Assertions.assertThat(replaceAnime)
                .isNotNull();

        Assertions.assertThat(replaceAnime.getStatusCode())
                .isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    @DisplayName("delete deletes anime when successful")
    void delete_DeletesAnime_WhenSuccessful() {

        Assertions.assertThatCode(() -> animeController.delete(1L))
                .doesNotThrowAnyException();

        ResponseEntity<Void> deleteAnime = animeController.delete(1L);

        Assertions.assertThat(deleteAnime)
                .isNotNull();

        Assertions.assertThat(deleteAnime.getStatusCode())
                .isEqualTo(HttpStatus.NO_CONTENT);

    }

}