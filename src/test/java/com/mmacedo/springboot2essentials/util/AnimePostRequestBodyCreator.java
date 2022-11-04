package com.mmacedo.springboot2essentials.util;

import com.mmacedo.springboot2essentials.domain.Anime;
import com.mmacedo.springboot2essentials.requests.AnimePostRequestBody;

public class AnimePostRequestBodyCreator {

    public static AnimePostRequestBody createAnimeToBeSaved() {
        return AnimePostRequestBody.builder()
                .name(AnimeCreator.createAnimeToBeSaved().getName())
                .build();
    }
}
