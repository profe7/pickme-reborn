package me.pick.metrodata.services.email;

import me.pick.metrodata.models.entity.Account;

public interface EmailService {
    void sendNewTalentCredentials(Account account, String password);
}
