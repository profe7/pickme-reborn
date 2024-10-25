package me.pick.metrodata.services.email;

import me.pick.metrodata.models.entity.Account;
import me.pick.metrodata.models.entity.InterviewSchedule;

public interface EmailService {
    void sendNewTalentCredentials(Account account, String password);

    void sendResetPasswordMessage (Account account, String url, String token);

    void sendInterviewInvitation(InterviewSchedule schedule);
}
