package me.pick.metrodata.services.account;

import me.pick.metrodata.models.dto.requests.AccountRequest;
import me.pick.metrodata.models.entity.Account;
import org.springframework.data.domain.Page;

public interface AccountService {
    Account createAccount(AccountRequest accountRequest);

    Page<Account> getAllAvailableAccounts(Integer page, Integer size);
}
