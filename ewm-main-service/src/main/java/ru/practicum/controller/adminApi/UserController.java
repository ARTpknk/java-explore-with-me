package ru.practicum.controller.adminApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.user.UserDto;
import ru.practicum.exception.ExploreWithMeBadRequest;
import ru.practicum.exception.ExploreWithMeNotFoundException;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.User;
import ru.practicum.service.user.UserService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userService.create(user));
    }

    @GetMapping("/{ids}")
    public List<UserDto> getUsers(@PathVariable("ids") List<Long> ids,
                                  @RequestParam(required = false, defaultValue = "0") int from,
                                  @RequestParam(required = false, defaultValue = "20") int size) {
        if (from < 0 || size < 1) {
            throw new ExploreWithMeBadRequest("некорректные значения");
        }
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }
        return userService.getUsers(ids, from, size)
                .stream().map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable("userId") Long userId) {
        if (userService.getUserById(userId) == null) {
            throw new ExploreWithMeNotFoundException("User with Id: " + userId + " not found");
        }
        userService.deleteUserById(userId);
    }
}