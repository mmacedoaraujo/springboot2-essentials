package com.mmacedo.springboot2essentials.integration;

import com.mmacedo.springboot2essentials.domain.Anime;
import com.mmacedo.springboot2essentials.domain.DevDojoUser;
import com.mmacedo.springboot2essentials.repository.AnimeRepository;
import com.mmacedo.springboot2essentials.repository.DevDojoUserRepository;
import com.mmacedo.springboot2essentials.requests.AnimePostRequestBody;
import com.mmacedo.springboot2essentials.util.AnimeCreator;
import com.mmacedo.springboot2essentials.util.AnimePostRequestBodyCreator;
import com.mmacedo.springboot2essentials.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AnimeControllerIT {
    @Autowired
    @Qualifier(value = "testRestTemplateRoleUser")
    private TestRestTemplate testRestTemplateRoleUser;
    @Autowired
    @Qualifier(value = "testRestTemplateRoleAdmin")
    private TestRestTemplate testRestTemplateRoleAdmin;
    @Autowired
    private AnimeRepository animeRepository;
    @Autowired
    private DevDojoUserRepository devDojoUserRepository;
    private static final DevDojoUser USER = DevDojoUser.builder()
            .username("marcos")
            .password("{bcrypt}$2a$10$EnDmRNO7jM0/U4trPdc7NufPODLiVCNqqV/Jb47uPTKFw3shTrlU.")
            .authorities("ROLE_USER")
            .build();
    private static final DevDojoUser ADMIN = DevDojoUser.builder()
            .username("devdojo")
            .password("{bcrypt}$2a$10$EnDmRNO7jM0/U4trPdc7NufPODLiVCNqqV/Jb47uPTKFw3shTrlU.")
            .authorities("ROLE_USER,ROLE_ADMIN")
            .build();

    @TestConfiguration
    @Lazy
    static class Config {
        @Bean(name = "testRestTemplateRoleUser")
        public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateUser = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port)
                    .basicAuthentication("marcos", "password");

            return new TestRestTemplate(restTemplateUser);
        }

        @Bean(name = "testRestTemplateRoleAdmin")
        public TestRestTemplate testRestTemplateRoleAdminCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateAdmin = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port)
                    .basicAuthentication("devdojo", "password");

            return new TestRestTemplate(restTemplateAdmin);
        }
    }

    @Test
    @DisplayName("List returns list of anime inside page object when successful")
    void list_ReturnsListOfAnimeInsidePageObject_WhenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        devDojoUserRepository.save(USER);

        PageableResponse<Anime> animePage = testRestTemplateRoleUser.exchange("/animes", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Anime>>() {
                }).getBody();

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

    @Test
    @DisplayName("Listall return list of anime when successful")
    void listAll_ReturnsListOfAnime_WhenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        devDojoUserRepository.save(USER);

        List<Anime> animes = testRestTemplateRoleUser.exchange("/animes/all", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animes.get(0).getName())
                .isEqualTo(savedAnime.getName());

        Assertions.assertThat(animes.get(0).getId())
                .isEqualTo(savedAnime.getId());
    }

    @Test
    @DisplayName("FindById returns a valid anime when successful")
    void findById_ReturnsValidAnime_WhenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        devDojoUserRepository.save(USER);

        Long expectedId = savedAnime.getId();

        Anime foundAnime = testRestTemplateRoleUser.getForObject("/animes/{id}", Anime.class, expectedId);

        Assertions.assertThat(foundAnime)
                .isNotNull();

        Assertions.assertThat(foundAnime.getId())
                .isNotNull()
                .isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName returns a list of animes when successful")
    void findByName_ReturnsListOfAnime_WhenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        devDojoUserRepository.save(USER);

        Long expectedId = savedAnime.getId();

        String expectedName = savedAnime.getName();

        String url = String.format("/animes/find?name=%s", expectedName);

        List<Anime> animes = testRestTemplateRoleUser.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animes.get(0).getId())
                .isEqualTo(expectedId);

        Assertions.assertThat(animes.get(0).getName())
                .isEqualTo(expectedName);

    }

    @Test
    @DisplayName("findByName returns an empty list of anime when anime is not found")
    void findByName_ReturnsEmptyListOfAnime_WhenAnimeIsNotFound() {
        devDojoUserRepository.save(USER);
        List<Anime> animes = testRestTemplateRoleUser.exchange("/animes/find?name=dbz", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isEmpty();

    }

    @Test
    @DisplayName("save returns anime when successful")
    void save_ReturnAnime_WhenSuccessful() {
        AnimePostRequestBody animePostRequestBody = AnimePostRequestBodyCreator.createAnimeToBeSaved();

        devDojoUserRepository.save(USER);

        ResponseEntity<Anime> animeResponseEntity = testRestTemplateRoleUser.postForEntity("/animes", animePostRequestBody, Anime.class);


        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(animeResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(animeResponseEntity.getBody().getId()).isNotNull();
    }


    @Test
    @DisplayName("replace updates anime when successful")
    void replace_UpdatesAnime_WhenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createValidAnime());

        devDojoUserRepository.save(USER);

        savedAnime.setName("new name");

        ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleUser.exchange("/animes",
                HttpMethod.PUT,
                new HttpEntity<>(savedAnime),
                Void.class);

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete returns unauthorized when successful")
    void delete_ReturnUnauthorized_WhenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createValidAnime());

        devDojoUserRepository.save(USER);

        ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleUser.exchange("/animes/admin/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                savedAnime.getId());

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
    @Test
    @DisplayName("delete deletes anime when successful")
    void delete_DeletesAnime_WhenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createValidAnime());

        devDojoUserRepository.save(ADMIN);

        ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleAdmin.exchange("/animes/admin/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                savedAnime.getId());

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

}


