package me.pick.metrodata.services.user;

import me.pick.metrodata.exceptions.user.UserDoesNotExistException;
import me.pick.metrodata.models.entity.User;
import me.pick.metrodata.repositories.UserRepository;
import me.pick.metrodata.utils.AuthUtil;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getLoggedUserData() {
        Long userId = AuthUtil.getLoginUserId();
        System.out.println ("Jalan le user");
        System.out.println (userId);
        return userRepository.findUserById(userId).orElseThrow(() -> {
            assert userId != null;
            return new UserDoesNotExistException(userId.toString());
        });
    }

}
