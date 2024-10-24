package me.pick.metrodata.services.account;

import me.pick.metrodata.exceptions.account.AccountAlreadyExistException;
import me.pick.metrodata.exceptions.account.AccountDoesNotExistException;
import me.pick.metrodata.exceptions.role.RoleDoesNotExistException;
import me.pick.metrodata.models.dto.requests.AccountRequest;
import me.pick.metrodata.models.entity.Account;
import me.pick.metrodata.models.entity.Institute;
import me.pick.metrodata.models.entity.User;
import me.pick.metrodata.repositories.AccountRepository;
import me.pick.metrodata.repositories.InstituteRepository;
import me.pick.metrodata.repositories.RoleRepository;
import me.pick.metrodata.repositories.UserRepository;
import me.pick.metrodata.repositories.specifications.AccountSpecification;
import me.pick.metrodata.utils.AuthUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        return accountDataHelper(account, request, user);
    }

    public Account editAccount(Long id, AccountRequest request) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountDoesNotExistException(id.toString()));
        User user = userRepository.findById(account.getUser().getId()).orElseThrow(() -> new AccountDoesNotExistException(account.getUser().getId().toString()));
        return accountDataHelper(account, request, user);
    }

    public Page<Account> getAllAvailableAccounts(Integer page, Integer size, String search, Long institute, Long baseBudget, Long limitBudget) {
        List<Institute> institutes = new ArrayList<>();
        return accountRetrievalHelper(search, institute, baseBudget, limitBudget, institutes, page, size);
    }

    public Page<Account> getAvailableAccountsOfRm(Integer page, Integer size, String search, Long institute, Long baseBudget, Long limitBudget) {
        Account account = accountRepository.findById(AuthUtil.getLoginUserId()).orElseThrow(() -> new AccountDoesNotExistException(AuthUtil.getLoginUserId().toString()));
        User user = userRepository.findById(account.getUser().getId()).orElseThrow(() -> new AccountDoesNotExistException(account.getUser().getId().toString()));
        Long instituteId = user.getInstitute().getId();
        List<Institute> institutes = new ArrayList<>();
        institutes.add(account.getUser().getInstitute());
        institutes.add(instituteRepository.findInstituteById(instituteId).orElseThrow(() -> new RoleDoesNotExistException(instituteId)));
        return accountRetrievalHelper(search, institute, baseBudget, limitBudget, institutes, page, size);
    }

    private Page<Account> accountRetrievalHelper(String search, Long institute, Long baseBudget, Long limitBudget, List<Institute> institutes, Integer page, Integer size) {
        Specification<Account> spec = AccountSpecification.searchSpecification(search, institute, baseBudget, limitBudget, institutes);
        List<Account> accounts = accountRepository.findAll(spec);
        Pageable pageable = PageRequest.of(page, size);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), accounts.size());

        return new PageImpl<>(accounts.subList(start, end), pageable, accounts.size());
    }

    private Account accountDataHelper (Account account, AccountRequest request, User user) {
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
