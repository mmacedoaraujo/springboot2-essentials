package com.mmacedo.springboot2essentials.service;

import com.mmacedo.springboot2essentials.domain.Anime;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimeService {

    public static List<Anime> listAll() {
        return List.of(new Anime(1L, "Boku no hero"), new Anime(2L,"Berserk"));
    }
}
