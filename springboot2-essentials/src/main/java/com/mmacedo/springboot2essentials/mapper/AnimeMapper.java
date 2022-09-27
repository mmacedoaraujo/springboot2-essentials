package com.mmacedo.springboot2essentials.mapper;

import com.mmacedo.springboot2essentials.domain.Anime;
import com.mmacedo.springboot2essentials.requests.AnimePostRequestBody;
import com.mmacedo.springboot2essentials.requests.AnimePutRequestBody;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class AnimeMapper {
    public static final AnimeMapper INSTANCE = Mappers.getMapper(AnimeMapper.class);

    public abstract Anime toAnime(AnimePostRequestBody animePostRequestBody);
    public abstract Anime toAnime(AnimePutRequestBody animePutRequestBody);
}
