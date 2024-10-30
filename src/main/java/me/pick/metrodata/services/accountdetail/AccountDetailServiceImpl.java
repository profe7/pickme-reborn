package me.pick.metrodata.services.accountdetail;

import lombok.RequiredArgsConstructor;
import me.pick.metrodata.models.entity.Account;
import me.pick.metrodata.models.entity.AccountDetail;
import me.pick.metrodata.repositories.AccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountDetailServiceImpl implements AccountDetailService {
  private final AccountRepository accountRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Account user = accountRepository.findByUsernameOrUserEmail(username, username)
        .orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
    return new AccountDetail(user);
  }
}