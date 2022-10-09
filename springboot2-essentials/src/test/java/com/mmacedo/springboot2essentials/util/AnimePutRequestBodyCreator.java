package com.mmacedo.springboot2essentials.util;

import com.mmacedo.springboot2essentials.requests.AnimePutRequestBody;

public class AnimePutRequestBodyCreator {

    public static AnimePutRequestBody createAnimePutRequestBody() {
        return AnimePutRequestBody.builder()
                .name(AnimeCreator.createValidUpdatedAnime().getName())
                .id(AnimeCreator.createAnimeToBeSaved().getId())
                .build();
    }
}
