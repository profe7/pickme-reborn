package me.pick.metrodata.controllers.rest;

import me.pick.metrodata.models.entity.User;
import me.pick.metrodata.services.user.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class RestUserController {
    private final UserService userService;

    public RestUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user-data")
    public User getLoggedUserData() {
        return userService.getLoggedUserData();
    }
}