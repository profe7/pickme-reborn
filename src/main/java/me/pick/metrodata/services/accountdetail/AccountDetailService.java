package me.pick.metrodata.services.accountdetail;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AccountDetailService extends UserDetailsService {
	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
