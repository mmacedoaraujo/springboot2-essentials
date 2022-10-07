package com.mmacedo.springboot2essentials.controller;

import com.mmacedo.springboot2essentials.domain.Anime;
import com.mmacedo.springboot2essentials.service.AnimeService;
import com.mmacedo.springboot2essentials.util.AnimeCreator;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
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

}