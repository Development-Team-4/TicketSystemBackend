package development.team.ticketsystem.auth_service.controller;

import development.team.ticketsystem.auth_service.dto.user.UpdateUserRequest;
import development.team.ticketsystem.auth_service.dto.user.UserResponse;
import development.team.ticketsystem.auth_service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable UUID id) {
        return new UserResponse();
    }

    @GetMapping
    public List<UserResponse> getUsers() {
        return new ArrayList<>();
    }

    @PutMapping("/{id}")
    public UserResponse updateUser(
            @PathVariable UUID id,
            @RequestBody UpdateUserRequest request
    ) {

        return new UserResponse();
    }

}

