package me.pick.metrodata.services.email;

import jakarta.mail.internet.MimeMessage;
import me.pick.metrodata.exceptions.email.EmailFailedToSendException;
import me.pick.metrodata.models.entity.Account;
import me.pick.metrodata.models.entity.InterviewSchedule;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public EmailServiceImpl(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    @Async
    public void sendNewTalentCredentials(Account account, String password) {
        try {
            MimeMessage message = mailSender.createMimeMessage ();
            MimeMessageHelper helper = new MimeMessageHelper (message, true, "UTF-8");

            helper.setTo (account.getUser().getEmail());
            helper.setSubject ("Credential Akun Metrodata PICK-ME Anda");

            Map<String, Object> templateModel = new HashMap<>();
            templateModel.put ("name", account.getUser ().getFirstName ());
            templateModel.put ("username", account.getUsername ());
            templateModel.put ("password", password);

            Context context = new Context ();
            context.setVariables (templateModel);

            String htmlContent = templateEngine.process ("email/new-talent-account", context);
            helper.setText (htmlContent, true);

            mailSender.send (message);
        } catch (Exception e) {
            throw new EmailFailedToSendException(account.getUser().getEmail());
        }
    }

    @Override
    @Async
    public void sendInterviewInvitation(InterviewSchedule schedule) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(schedule.getApplicant().getTalent().getEmail());
            helper.setCc(schedule.getApplicant().getTalent().getMitra().getUser().getEmail());
            helper.setSubject("Undangan Interview Untuk Posisi " + schedule.getPosition() + " di " + schedule.getClient().getUser().getInstitute().getInstituteName());

            Map<String, Object> templateModel = new HashMap<>();
            templateModel.put("talent", schedule.getApplicant().getTalent().getName());
            templateModel.put("date", schedule.getDate());
            templateModel.put("start", schedule.getStartTime());
            templateModel.put("end", schedule.getEndTime());
            templateModel.put("site", schedule.getClient().getUser().getInstitute().getInstituteName());
            templateModel.put("position", schedule.getPosition());
            templateModel.put("type", schedule.getInterviewType());
            templateModel.put("link", schedule.getInterviewLink());
            templateModel.put("message", schedule.getMessage());

            Context context = new Context();
            context.setVariables(templateModel);

            String htmlContent = templateEngine.process("email/invitation", context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new EmailFailedToSendException(schedule.getApplicant().getTalent().getEmail());
        }
    }

    @Override
    @Async
    public void sendResetPasswordMessage (Account account, String url, String token) {
        try {
            //			String resetPasswordUrl = url + "/reset-password/" + token;
            String resetPasswordUrl = "http://localhost:9002/change-password/" + token;
            MimeMessage message = mailSender.createMimeMessage ();
            MimeMessageHelper helper = new MimeMessageHelper (message, true);

            helper.setTo (account.getUser ().getEmail ());
            helper.setSubject ("Reset Password By PICKME");

            Context context = new Context ();
            context.setVariable ("name", account.getUser ().getFirstName ());
            context.setVariable ("resetPasswordUrl", resetPasswordUrl);

            String htmlContent = templateEngine.process ("email/reset-password", context);
            helper.setText (htmlContent, true);

            mailSender.send (message);
        } catch (Exception e) {
            throw new EmailFailedToSendException(account.getUser().getEmail());
        }
    }
}
