package me.pick.metrodata.services.user;

import lombok.RequiredArgsConstructor;
import me.pick.metrodata.exceptions.user.UserDoesNotExistException;
import me.pick.metrodata.models.entity.Account;
import me.pick.metrodata.models.entity.User;
import me.pick.metrodata.repositories.UserRepository;
import me.pick.metrodata.services.account.AccountService;
import me.pick.metrodata.utils.AuthUtil;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AccountService accountService;

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
        return userRepository.findUserById(id).orElseThrow(() -> new UserDoesNotExistException(id.toString()));
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> usersWithSpecificPrivileges() {
        return accountService.getAll().stream()
                .filter(account -> account.getRole().getPrivileges().stream()
                        .anyMatch(privilege -> "MANAGEMENT_READ_ACCOUNT".equals(privilege.getName()) ||
                                "EXTERNAL_READ_ACCOUNT".equals(privilege.getName())))
                .map(Account::getUser)
                .collect(Collectors.toList());
    }
}
