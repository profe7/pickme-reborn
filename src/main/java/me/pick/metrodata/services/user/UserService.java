package me.pick.metrodata.services.user;

import me.pick.metrodata.models.entity.User;

public interface UserService {
    User getLoggedUserData();

    User getById(Long id);
}
