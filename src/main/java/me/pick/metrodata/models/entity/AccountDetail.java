package me.pick.metrodata.models.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountDetail implements UserDetails {

    private Account account;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        Role role = account.getRole();
        authorities.add(new SimpleGrantedAuthority("ROLE_"
                + role.getName().toUpperCase()));
        role.getPrivileges().forEach(privilege -> {
            authorities.add(new SimpleGrantedAuthority(privilege.getName().toUpperCase()));
        });

        return authorities;
    }

    public Long getId() {
        return account.getId();
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return account.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return account.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return account.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return account.isEnabled();
    }

}
