package me.pick.metrodata.services.account;

import me.pick.metrodata.exceptions.account.AccountAlreadyExistException;
import me.pick.metrodata.exceptions.role.RoleDoesNotExistException;
import me.pick.metrodata.models.dto.requests.AccountRequest;
import me.pick.metrodata.models.entity.Account;
import me.pick.metrodata.models.entity.User;
import me.pick.metrodata.repositories.AccountRepository;
import me.pick.metrodata.repositories.InstituteRepository;
import me.pick.metrodata.repositories.RoleRepository;
import me.pick.metrodata.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final InstituteRepository instituteRepository;
    private final UserRepository userRepository;

    public AccountServiceImpl(AccountRepository accountRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, InstituteRepository instituteRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.instituteRepository = instituteRepository;
        this.userRepository = userRepository;
    }

    public Account createAccount(AccountRequest request) {
        Account existing = accountRepository.findByUsername(request.getAccountUsername()).orElse(null);
        if (existing != null) {
            throw new AccountAlreadyExistException(request.getAccountUsername());
        }

        Account account = new Account();
        User user  = new User();

        account.setUsername(request.getAccountUsername());
        account.setPassword(passwordEncoder.encode(request.getAccountPassword()));
        account.setRole(roleRepository.findById(request.getRoleId()).orElseThrow(() -> new RoleDoesNotExistException(request.getRoleId())));

        user.setFirstName(request.getAccountFirstName());
        user.setLastName(request.getAccountLastName());
        user.setEmail(request.getAccountEmail());
        user.setInstitute(instituteRepository.findInstituteById(request.getInstituteId()).orElseThrow(() -> new RoleDoesNotExistException(request.getInstituteId())));

        userRepository.save(user);
        account.setUser(user);
        return accountRepository.save(account);
    }
}
