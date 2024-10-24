package me.pick.metrodata.services.account;

import me.pick.metrodata.models.dto.requests.AccountRequest;
import me.pick.metrodata.models.entity.Account;
import org.springframework.data.domain.Page;

public interface AccountService {
    Account createAccount(AccountRequest accountRequest);

    Account editAccount(Long id, AccountRequest accountRequest);

    Account getAccountById(Long id);

    Page<Account> getAllAvailableAccounts(Integer page, Integer size, String search, Long institute, Long baseBudget, Long limitBudget);

    Page<Account> getAvailableAccountsOfRm(Integer page, Integer size, String search, Long institute, Long baseBudget, Long limitBudget);
}
