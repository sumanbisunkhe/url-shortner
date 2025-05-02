package com.example.urlshortner.mapper;


import com.example.urlshortner.dto.request.UrlRequest;
import com.example.urlshortner.dto.response.UrlResponse;
import com.example.urlshortner.model.ShortUrl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UrlMapper {
    UrlMapper INSTANCE = Mappers.getMapper(UrlMapper.class);

    @Mapping(target = "clickCount", ignore = true)
    UrlResponse toResponse(ShortUrl shortUrl);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "shortCode", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "clickCount", ignore = true)
    @Mapping(target = "expirationDate", ignore = true)
    @Mapping(target = "active", ignore = true)
    ShortUrl toEntity(UrlRequest urlRequest);
}