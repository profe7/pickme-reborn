package me.pick.metrodata.Repositories;

import me.pick.metrodata.models.entity.ResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Long> {
    public Optional<ResetPasswordToken> findByToken(String token);
}
