package com.mmacedo.springboot2essentials.util;

import com.mmacedo.springboot2essentials.requests.AnimePutRequestBody;

public class AnimePutRequestBodyCreator {

    public static AnimePutRequestBody createAnimePutRequestBody() {
        return AnimePutRequestBody.builder()
                .name(AnimeCreator.createValidAnime().getName())
                .id(AnimeCreator.createValidAnime().getId())
                .build();
    }
}
