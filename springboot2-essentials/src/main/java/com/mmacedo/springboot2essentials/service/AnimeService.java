package com.mmacedo.springboot2essentials.service;

import com.mmacedo.springboot2essentials.domain.Anime;
import com.mmacedo.springboot2essentials.repository.AnimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class AnimeService {
    private final AnimeRepository animeRepository;
    public List<Anime> listAll() {
        return animeRepository.findAll();
    }

    public Anime findById(Long id) {
        return animeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Anime not found"));
    }

//    public Anime save(Anime anime) {
//        anime.setId(ThreadLocalRandom.current().nextLong(1, 100000));
//        animes.add(anime);
//
//        return anime;
//    }
//
//    public void delete(Long id) {
//        animes.remove(findById(id));
//    }
//
//    public void replace(Anime anime) {
//        delete(anime.getId());
//        animes.add(anime);
//    }
}
