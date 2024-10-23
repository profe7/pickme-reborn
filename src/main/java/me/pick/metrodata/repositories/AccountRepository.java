package me.pick.metrodata.repositories;

import me.pick.metrodata.models.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {

        Optional<Account> findByUsername(String username);

        Optional<Account> findByUsernameOrUserEmail(String username, String email);
}