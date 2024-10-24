package me.pick.metrodata.services.account;

import me.pick.metrodata.models.dto.requests.AccountRequest;
import me.pick.metrodata.models.entity.Account;

public interface AccountService {
    Account createAccount(AccountRequest accountRequest);
}
