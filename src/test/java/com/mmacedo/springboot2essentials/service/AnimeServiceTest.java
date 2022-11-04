package com.mmacedo.springboot2essentials.service;

import com.mmacedo.springboot2essentials.domain.Anime;
import com.mmacedo.springboot2essentials.exceptions.BadRequestException;
import com.mmacedo.springboot2essentials.repository.AnimeRepository;
import com.mmacedo.springboot2essentials.requests.AnimePutRequestBody;
import com.mmacedo.springboot2essentials.util.AnimeCreator;
import com.mmacedo.springboot2essentials.util.AnimePostRequestBodyCreator;
import com.mmacedo.springboot2essentials.util.AnimePutRequestBodyCreator;
import com.mmacedo.springboot2essentials.util.DateUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class AnimeServiceTest {

    @InjectMocks
    private AnimeService animeService;
    @Mock
    private AnimeRepository animeRepository;
    @Mock
    private DateUtil dateUtil;

    @BeforeEach
    void setUp() {
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
        BDDMockito.when(animeRepository.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(animePage);

        BDDMockito.when(dateUtil.formatLocalDateTimeToDatabaseStyle(ArgumentMatchers.any()))
                .thenReturn(String.valueOf(LocalDateTime.now()));

        BDDMockito.when(animeRepository.findAll())
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeRepository.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeRepository.save(ArgumentMatchers.any(Anime.class)))
                .thenReturn(AnimeCreator.createValidAnime());

        BDDMockito.doNothing().when(animeRepository)
                .delete(ArgumentMatchers.any(Anime.class));

    }

    @Test
    @DisplayName("listAll returns list of anime inside page object when successful")
    void listAll_ReturnsListOfAnimeInsidePageObject_WhenSuccessful() {
        Anime validAnime = AnimeCreator.createValidAnime();
        Page<Anime> animePage = animeService.listAll(PageRequest.of(1, 1));

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
    @DisplayName("listAllNonPageable return list of anime when successful")
    void listAllNonPageable_ReturnsListOfAnime_WhenSuccessful() {
        Anime testAnime = AnimeCreator.createValidAnime();

        List<Anime> animes = animeService.listAllNonPageable();

        Assertions.assertThat(animes)
                .isNotEmpty()
                .isNotNull()
                .hasSize(1);
        Assertions.assertThat(animes.get(0).getName())
                .isEqualTo(testAnime.getName());
    }

    @Test
    @DisplayName("FindByIdOrThrowBadRequestException returns a valid anime when successful")
    void findByIdOrThrowBadRequestException_ReturnsAnValidAnime_WhenSuccessful() {
        Anime comparisonAnime = AnimeCreator.createValidAnime();

        Anime foundAnime = animeService.findByIdOrThrowBadRequestException(2L);

        Assertions.assertThat(foundAnime)
                .isNotNull()
                .isInstanceOf(Anime.class);

        Assertions.assertThat(foundAnime.getId())
                .isNotNull()
                .isEqualTo(comparisonAnime.getId());
    }

    @Test
    @DisplayName("FindByIdOrThrowBadRequestException throws BadRequestException when anime is not found")
    void findByIdOrThrowBadRequestException_ThrowsBadRequestException_WhenAnimeIsNotFound() {
        BDDMockito.when(animeRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> this.animeService.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()));

    }

    @Test
    @DisplayName("findByName returns a list of animes when successful")
    void findByName_ReturnsListOfAnime_WhenSuccessful() {
        Anime comparisonAnime = AnimeCreator.createValidAnime();

        List<Anime> animes = animeService.findByName("a");

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
        BDDMockito.when(animeRepository.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Anime> anime = animeService.findByName("anime");

        Assertions.assertThat(anime)
                .isNotNull()
                .isEmpty();


    }

    @Test
    @DisplayName("save returns anime when successful")
    void save_ReturnAnime_WhenSuccessful() {

        Anime anime = animeService.save(AnimePostRequestBodyCreator.createAnimeToBeSaved());

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

        Assertions.assertThatCode(() -> this.animeService.replace(AnimePutRequestBodyCreator.createAnimePutRequestBody()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("delete deletes anime when successful")
    void delete_DeletesAnime_WhenSuccessful() {

        Assertions.assertThatCode(() -> animeService.delete(1L))
                .doesNotThrowAnyException();

    }

}