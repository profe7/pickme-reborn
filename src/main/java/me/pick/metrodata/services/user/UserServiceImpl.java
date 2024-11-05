package me.pick.metrodata.services.user;

import lombok.RequiredArgsConstructor;
import me.pick.metrodata.exceptions.user.UserDoesNotExistException;
import me.pick.metrodata.models.entity.User;
import me.pick.metrodata.repositories.UserRepository;
import me.pick.metrodata.utils.AuthUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getLoggedUserData() {
        Long userId = AuthUtil.getLoginUserId();
        return userRepository.findUserById(userId).orElseThrow(() -> {
            assert userId != null;
            return new UserDoesNotExistException(userId.toString());
        });
    }

    @Override
    public User getById(Long id) {
        return userRepository.findUserById(id).orElse(null);
    }
}
