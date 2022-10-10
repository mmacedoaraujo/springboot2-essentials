package com.mmacedo.springboot2essentials.integration;

import com.mmacedo.springboot2essentials.domain.Anime;
import com.mmacedo.springboot2essentials.repository.AnimeRepository;
import com.mmacedo.springboot2essentials.util.AnimeCreator;
import com.mmacedo.springboot2essentials.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class AnimeControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @LocalServerPort
    private int port;
    @Autowired
    private AnimeRepository animeRepository;

    @Test
    @DisplayName("List returns list of anime inside page object when successful")
    void list_ReturnsListOfAnimeInsidePageObject_WhenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        PageableResponse<Anime> animePage = testRestTemplate.exchange("/animes", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Anime>>() {}).getBody();

        Assertions.assertThat(animePage)
                .isNotNull();

        Assertions.assertThat(animePage.toList())
                .isNotNull()
                .hasSize(1);

        Assertions.assertThat(animePage.toList().get(0).getName())
                .isEqualTo(savedAnime.getName());

        Assertions.assertThat(animePage.toList().get(0).getId())
                .isEqualTo(savedAnime.getId());
    }

}
