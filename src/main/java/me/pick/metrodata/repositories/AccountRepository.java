package me.pick.metrodata.repositories;

import me.pick.metrodata.models.entity.Account;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {

        Optional<Account> findByUsername(String username);

        Optional<Account> findByUsernameOrUserEmail(String username, String email);

        @Query("SELECT a FROM Account a WHERE "
                        + "(:searchUsername IS NULL OR a.username LIKE %:searchUsername%) AND "
                        + "(:role IS NULL OR a.role.id = :role)")
        Page<Account> findAllWithFilters(
                        @Param("searchUsername") String searchUsername,
                        @Param("role") Long role,
                        Pageable pageable);

        @Query("SELECT a FROM Account a WHERE "
                        + "(:searchUsername IS NULL OR a.username LIKE %:searchUsername%) AND "
                        + "(:role IS NULL OR a.role.id = :role) AND "
                        + "(:isEnabled IS NULL OR a.isEnabled = :isEnabled)")
        Page<Account> findAllWithFiltersWithStatus(
                        @Param("searchUsername") String searchUsername,
                        @Param("role") Long role,
                        @Param("isEnabled") boolean isEnabled,
                        Pageable pageable);
}
