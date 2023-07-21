package ru.practicum.controller.adminApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.user.UserDto;
import ru.practicum.exception.ExploreWithMeNotFoundException;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.user.User;
import ru.practicum.service.user.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userService.create(user));
    }

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                  @RequestParam(required = false, defaultValue = "0") int from,
                                  @RequestParam(required = false, defaultValue = "10") int size) {
        if (ids == null) {
            return userService.getUsers(from, size).stream().map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        }
        if (ids.isEmpty()) {
            return userService.getUsers(from, size).stream().map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        }
        return userService.getUsersByIds(ids, from, size)
                .stream().map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable("userId") Long userId) {
        if (userService.getUserById(userId) == null) {
            throw new ExploreWithMeNotFoundException("User with Id: " + userId + " not found");
        }
        userService.deleteUserById(userId);
    }
}