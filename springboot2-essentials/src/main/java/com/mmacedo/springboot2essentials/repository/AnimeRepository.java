package com.mmacedo.springboot2essentials.repository;

import com.mmacedo.springboot2essentials.domain.Anime;

import java.util.List;

public interface AnimeRepository {
    List<Anime> listAll();
}
