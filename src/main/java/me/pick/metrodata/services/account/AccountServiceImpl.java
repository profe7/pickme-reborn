package me.pick.metrodata.services.account;

import org.modelmapper.ModelMapper;

import lombok.RequiredArgsConstructor;
import me.pick.metrodata.exceptions.account.AccountAlreadyExistException;
import me.pick.metrodata.exceptions.account.AccountDoesNotExistException;
import me.pick.metrodata.models.dto.requests.AccountRequest;
import me.pick.metrodata.models.dto.responses.AccountResponse;
import me.pick.metrodata.models.entity.Account;
import me.pick.metrodata.models.entity.Institute;
import me.pick.metrodata.models.entity.User;
import me.pick.metrodata.repositories.AccountRepository;
import me.pick.metrodata.repositories.UserRepository;
import me.pick.metrodata.repositories.specifications.AccountSpecification;
import me.pick.metrodata.services.institute.InstituteService;
import me.pick.metrodata.services.role.RoleService;
import me.pick.metrodata.utils.AuthUtil;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

        private final AccountRepository accountRepository;
        private final RoleService roleService;
        private final InstituteService instituteService;
        private final UserRepository userRepository;
        private final ModelMapper modelMapper;

        @Override
        public void create(AccountRequest accountRequest) {
                accountRepository.findByUsername(accountRequest.getUsername()).ifPresent(username -> {
                        throw new AccountAlreadyExistException(accountRequest.getUsername());
                });
                Account account = modelMapper.map(accountRequest, Account.class);
                User user = modelMapper.map(accountRequest, User.class);
                accountDataHelper(account, accountRequest, user);
        }

        @Override
        public void update(Long id, AccountRequest accountRequest) {
                Account account = accountRepository.findById(id)
                                .orElseThrow(() -> new AccountDoesNotExistException(id.toString()));
                Long userId = account.getUser().getId();
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new AccountDoesNotExistException(userId.toString()));

                account.setUsername(accountRequest.getUsername());
                account.setPassword(accountRequest.getPassword());
                user.setFirstName(accountRequest.getFirstName());
                user.setLastName(accountRequest.getLastName());
                user.setEmail(accountRequest.getEmail());
                user.setPhone(accountRequest.getPhone());
                user.setBaseBudget(accountRequest.getBaseBudget());
                user.setLimitBudget(accountRequest.getLimitBudget());

                accountDataHelper(account, accountRequest, user);
        }

        @Override
        public void updateProfile(Long id, AccountRequest accountRequest) {
                Account account = accountRepository.findById(id)
                                .orElseThrow(() -> new AccountDoesNotExistException(id.toString()));
                Long userId = account.getUser().getId();
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new AccountDoesNotExistException(userId.toString()));

                account.setUsername(accountRequest.getUsername());
                user.setFirstName(accountRequest.getFirstName());
                user.setLastName(accountRequest.getLastName());
                user.setEmail(accountRequest.getEmail());
                user.setPhone(accountRequest.getPhone());

                account.setUser(user);
                user.setAccount(account);

                userRepository.save(user);
                accountRepository.save(account);
        }

        @Override
        public Page<Account> getAllAvailableAccounts(Integer page, Integer size, String search, Long institute,
                        Long baseBudget, Long limitBudget) {
                List<Institute> institutes = new ArrayList<>();
                return accountRetrievalHelper(search, institute, baseBudget, limitBudget, institutes, page, size);
        }

        @Override
        public Page<Account> getAvailableAccountsOfRm(Integer page, Integer size, String search, Long institute,
                        Long baseBudget, Long limitBudget) {
                Account account = accountRepository.findById(AuthUtil.getLoginUserId())
                                .orElseThrow(() -> new AccountDoesNotExistException(
                                                AuthUtil.getLoginUserId().toString()));
                User user = userRepository.findById(account.getUser().getId())
                                .orElseThrow(() -> new AccountDoesNotExistException(
                                                account.getUser().getId().toString()));
                Long instituteId = user.getInstitute().getId();
                List<Institute> institutes = new ArrayList<>();
                institutes.add(account.getUser().getInstitute());
                institutes.add(instituteService.getInstituteById(instituteId));
                return accountRetrievalHelper(search, institute, baseBudget, limitBudget, institutes, page, size);
        }

        @Override
        public AccountResponse getAccountById(Long id) {
                return accountRepository.findById(id).map(account -> {
                        AccountResponse accountResponse = modelMapper.map(account, AccountResponse.class);
                        accountResponse.setFirstName(account.getUser().getFirstName());
                        accountResponse.setLastName(account.getUser().getLastName());
                        accountResponse.setPhone(account.getUser().getPhone());
                        accountResponse.setBaseBudget(account.getUser().getBaseBudget());
                        accountResponse.setLimitBudget(account.getUser().getLimitBudget());
                        accountResponse.setEmail(account.getUser().getEmail());
                        accountResponse.setInstituteName(account.getUser().getInstitute().getInstituteName());
                        accountResponse.setRoleName(account.getRole().getName());
                        accountResponse.setInstituteId(account.getUser().getInstitute().getId());
                        accountResponse.setRoleId(account.getRole().getId());
                        return accountResponse;
                }).orElse(null);
        }

        private Page<Account> accountRetrievalHelper(String search, Long institute, Long baseBudget, Long limitBudget,
                        List<Institute> institutes, Integer page, Integer size) {
                Specification<Account> spec = AccountSpecification.searchSpecification(search, institute, baseBudget,
                                limitBudget, institutes);
                List<Account> accounts = accountRepository.findAll(spec);
                Pageable pageable = PageRequest.of(page, size);

                int start = (int) pageable.getOffset();
                int end = Math.min((start + pageable.getPageSize()), accounts.size());

                return new PageImpl<>(accounts.subList(start, end), pageable, accounts.size());
        }

        private void accountDataHelper(Account account, AccountRequest request, User user) {
                account.setRole(roleService.getRoleById(request.getRoleId()));
                user.setInstitute(instituteService.getInstituteById(request.getInstituteId()));
                account.setUser(user);
                user.setAccount(account);

                userRepository.save(user);
                accountRepository.save(account);
        }

        @Override
        public Page<AccountResponse> getFilteredAccount(String searchUsername, Long role, String status, Integer page,
                        Integer size) {
                Pageable pageable = PageRequest.of(page, size);
                boolean isEnabled = "AKTIF".equals(status);
                Page<Account> accounts = !"".equals(status)
                                ? accountRepository.findAllWithFiltersWithStatus(searchUsername, role, isEnabled,
                                                pageable)
                                : accountRepository
                                                .findAllWithFilters(searchUsername, role, pageable);
                return accounts.map(account -> {
                        AccountResponse accountResponse = modelMapper.map(account,
                                        AccountResponse.class);
                        accountResponse.setEmail(account.getUser().getEmail());
                        accountResponse.setInstituteName(account.getUser().getInstitute().getInstituteName());
                        accountResponse.setAccess(account.isAccountNonLocked());
                        accountResponse.setRoleName(account.getRole().getName());
                        accountResponse.setStatus(account.isEnabled());
                        return accountResponse;
                });
        }

        @Override
        public List<Account> getAll() {
                return accountRepository.findAll();
        }

        @Override
        public void updateAccess(Long id) {
                accountRepository.findById(id).map(account -> {
                        account.setEnabled(!account.isEnabled());
                        return accountRepository.save(account);
                }).orElseThrow(() -> new AccountDoesNotExistException(id.toString()));
        }
}
