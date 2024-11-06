package me.pick.metrodata.services.email;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import me.pick.metrodata.enums.InterviewStatus;
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
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    private static final String UTF = "UTF-8";
    private static final String TALENT = "talent";
    private static final String INTERVIEW = "Interview Anda untuk posisi ";

    @Override
    @Async
    public void sendNewTalentCredentials(Account account, String password) {
        try {
            MimeMessage message = mailSender.createMimeMessage ();
            MimeMessageHelper helper = new MimeMessageHelper (message, true, UTF);

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
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF);

            helper.setTo(schedule.getApplicant().getTalent().getEmail());
            helper.setCc(schedule.getApplicant().getTalent().getMitra().getUser().getEmail());
            helper.setSubject("Undangan Interview Untuk Posisi " + schedule.getPosition() + " di " + schedule.getClient().getUser().getInstitute().getInstituteName());

            Map<String, Object> templateModel = templateHelperOne(schedule);

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
    public void sendInterviewReschedule(InterviewSchedule interviewSchedule) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF);

            Map<String, Object> template = templateHelperOne(interviewSchedule);

            Context context = new Context();
            context.setVariables(template);
            String html = templateEngine.process("email/reschedule", context);
            helper.setTo(interviewSchedule.getApplicant().getTalent().getEmail());
            helper.setText(html, true);
            helper.setSubject("Reschedule Interview Anda Untuk Posisi " + interviewSchedule.getPosition() + " di " + interviewSchedule.getClient().getUser().getInstitute().getInstituteName());

            mailSender.send(message);
        } catch (Exception e) {
            throw new EmailFailedToSendException(interviewSchedule.getApplicant().getTalent().getEmail());
        }
    }

    @Override
    @Async
    public void sendInterviewCancel(InterviewSchedule interviewSchedule, String feedback) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF);

            Map<String, Object> template = templateHelperTwo(interviewSchedule, feedback);

            Context context = new Context();
            context.setVariables(template);
            String html = templateEngine.process("email/cancelled", context);

            helper.setTo(interviewSchedule.getApplicant().getTalent().getEmail());
            helper.setText(html, true);
            helper.setSubject(INTERVIEW + interviewSchedule.getPosition() + " di " + interviewSchedule.getClient().getUser().getInstitute().getInstituteName() + " Dibatalkan");

            mailSender.send(message);
        } catch (Exception e) {
            throw new EmailFailedToSendException(interviewSchedule.getApplicant().getTalent().getEmail());
        }
    }

    @Override
    @Async
    public void sendInterviewAccept(InterviewSchedule interviewSchedule, String feedback) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF);

            Map<String, Object> template = templateHelperTwo(interviewSchedule, feedback);

            Context context = new Context();
            context.setVariables(template);
            String html = templateEngine.process("email/accepted", context);

            helper.setTo(interviewSchedule.getApplicant().getTalent().getEmail());
            helper.setText(html, true);
            helper.setSubject(INTERVIEW + interviewSchedule.getPosition() + " di " + interviewSchedule.getClient().getUser().getInstitute().getInstituteName() + " Diterima");

            mailSender.send(message);
        } catch (Exception e) {
            throw new EmailFailedToSendException(interviewSchedule.getApplicant().getTalent().getEmail());
        }
    }

    @Override
    @Async
    public void sendInterviewReject(InterviewSchedule interviewSchedule, String feedback) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF);

            Map<String, Object> template = templateHelperTwo(interviewSchedule, feedback);

            Context context = new Context();
            context.setVariables(template);
            String html = templateEngine.process("email/rejected", context);

            helper.setTo(interviewSchedule.getApplicant().getTalent().getEmail());
            helper.setText(html, true);
            helper.setSubject("Interview Anda Untuk Posisi " + interviewSchedule.getPosition() + " di " + interviewSchedule.getClient().getUser().getInstitute().getInstituteName() + " Ditolak");

            mailSender.send(message);
        } catch (Exception e) {
            throw new EmailFailedToSendException(interviewSchedule.getApplicant().getTalent().getEmail());
        }
    }

    @Override
    @Async
    public void sendResetPasswordMessage (Account account, String url, String token) {
        try {
            //			String resetPasswordUrl = url + "/reset-password/" + token;
            String resetPasswordUrl = "http://localhost:9002/change-password/" + token;
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(account.getUser().getEmail());
            helper.setSubject("Reset Password By PICKME");

            Context context = new Context();
            context.setVariable("name", account.getUser().getFirstName());
            context.setVariable("resetPasswordUrl", resetPasswordUrl);

            String htmlContent = templateEngine.process("email/reset-password", context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new EmailFailedToSendException(account.getUser().getEmail());
        }
    }

    private Map<String, Object> templateHelperOne(InterviewSchedule detail) {
        Map<String, Object> template = new HashMap<>();
        template.put(TALENT, detail.getApplicant ().getTalent ().getName());
        template.put("date", detail.getDate());
        template.put("start", detail.getStartTime());
        template.put("end", detail.getEndTime());
        template.put("site", detail.getClient ().getUser ().getInstitute ().getCompanyName ());
        template.put("position", detail.getPosition());
        template.put("type", detail.getInterviewType().toString());
        template.put("link", detail.getInterviewLink());
        template.put("location", detail.getLocationAddress());
        template.put("message", detail.getMessage());

        return template;
    }

    private Map<String, Object> templateHelperTwo(InterviewSchedule interviewSchedule, String feedback) {
        Map<String, Object> template = new HashMap<>();
        if (interviewSchedule.getStatus().equals(InterviewStatus.REJECTED) || interviewSchedule.getStatus().equals(InterviewStatus.CANCELLED)) {
            template.put(TALENT, interviewSchedule.getApplicant().getTalent().getName());
            template.put("site", interviewSchedule.getClient().getUser().getInstitute().getInstituteName());
            template.put("feedback", feedback);
        } else if (interviewSchedule.getStatus().equals(InterviewStatus.ACCEPTED)) {
            template.put(TALENT, interviewSchedule.getApplicant().getTalent().getName());
            template.put("site", interviewSchedule.getClient().getUser().getInstitute().getInstituteName());
            template.put("feedback", feedback);
            template.put("onBoardDate", interviewSchedule.getOnBoardDate());
        }

        return template;
    }

}
