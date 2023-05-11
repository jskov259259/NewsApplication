package ru.clevertec.kalustau.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.kalustau.model.JwtRequest;
import ru.clevertec.kalustau.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User requestToUser(JwtRequest jwtRequest);

}
