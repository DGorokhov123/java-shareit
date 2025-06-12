package ru.practicum.shareit.user;

import ru.practicum.shareit.user.api.UserCreateDto;
import ru.practicum.shareit.user.api.UserResponseDto;
import ru.practicum.shareit.user.api.UserUpdateDto;

import java.util.List;

public interface UserService {

    public List<UserResponseDto> getList();

    public UserResponseDto getById(Long userId);

    public UserResponseDto create(UserCreateDto user);

    public UserResponseDto update(UserUpdateDto user, Long userId);

    public void deleteById(Long userId);

}
