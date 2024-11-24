package me.pick.metrodata.services.user;

import java.util.List;

import me.pick.metrodata.models.entity.User;

public interface UserService {

    User getLoggedUserData();

    User getById(Long id);

    List<User> getAll();

    List<User> usersWithSpecificPrivileges();
}
