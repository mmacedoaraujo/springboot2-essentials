package com.mmacedo.springboot2essentials.client;

import com.mmacedo.springboot2essentials.domain.Anime;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {
        ResponseEntity<Anime> entity = new RestTemplate().getForEntity("http://localhost:8080/animes/{id}", Anime.class, 2);

        log.info(entity);

        Anime object = new RestTemplate().getForObject("http://localhost:8080/animes/{id}", Anime.class, 2);

        log.info(object);

        Anime[] animes = new RestTemplate().getForObject("http://localhost:8080/animes/all", Anime[].class);

        log.info(animes);

        ResponseEntity<List<Anime>> exchange = new RestTemplate().exchange("http://localhost:8080/animes/all", HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Anime>>() {
                });

        log.info(exchange);

//        Anime kingdom = new Anime().builder().name("Kingdom").build();
//        Anime savedAnimeKingdom = new RestTemplate().postForObject("http://localhost:8080/animes", kingdom, Anime.class);
//
//        log.info("saved {}", savedAnimeKingdom);

        Anime samuraiChamploo = Anime.builder().name("Samurai Champloo").build();
        ResponseEntity<Anime> savedAnimeSamuraiChamploo = new RestTemplate()
                .exchange("http://localhost:8080/animes",
                        HttpMethod.POST,
                        new HttpEntity<>(samuraiChamploo, createJsonHeader()),
                        Anime.class);

        log.info("Saved anime {}", savedAnimeSamuraiChamploo);

    }

    private static HttpHeaders createJsonHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}
