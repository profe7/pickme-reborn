package me.pick.metrodata.exceptions.advice;

import me.pick.metrodata.exceptions.applicant.ApplicantAlreadyExistsException;
import me.pick.metrodata.exceptions.applicant.ApplicantDoesNotExistException;
import me.pick.metrodata.exceptions.account.AccountAlreadyExistException;
import me.pick.metrodata.exceptions.account.AccountDoesNotExistException;
import me.pick.metrodata.exceptions.account.AccountInvalidPasswordException;
import me.pick.metrodata.exceptions.client.ClientDoesNotExistException;
import me.pick.metrodata.exceptions.institute.InstituteDoesNotExistException;
import me.pick.metrodata.exceptions.institute.InstituteTypeDoesNotExistException;
import me.pick.metrodata.exceptions.interviewschedule.ApplicantNotRecommendedException;
import me.pick.metrodata.exceptions.interviewschedule.InterviewScheduleConflictException;
import me.pick.metrodata.exceptions.interviewschedule.InterviewScheduleDoesNotExistException;
import me.pick.metrodata.exceptions.interviewschedule.InterviewScheduleUpdateBadRequestException;
import me.pick.metrodata.exceptions.mitra.MitraDoesNotExistException;
import me.pick.metrodata.exceptions.privilege.PrivilegeDoesNotExistException;
import me.pick.metrodata.exceptions.recommendation.RecommendationDoesNotExistException;
import me.pick.metrodata.exceptions.role.RoleDoesNotExistException;
import me.pick.metrodata.exceptions.talent.IncompleteTalentCvException;
import me.pick.metrodata.exceptions.talent.InvalidTalentNikException;
import me.pick.metrodata.exceptions.talent.TalentAlreadyExistException;
import me.pick.metrodata.exceptions.talent.TalentDoesNotExistException;
import me.pick.metrodata.exceptions.user.UserDoesNotExistException;
import me.pick.metrodata.exceptions.vacancy.IncompleteVacancyRequestException;
import me.pick.metrodata.exceptions.vacancy.VacancyNotExistException;
import me.pick.metrodata.exceptions.vacancy.VacancyStatusDoesNotExistException;
import me.pick.metrodata.utils.Response;
import me.pick.metrodata.utils.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String FAILED = "FAILED";

    @ExceptionHandler(value = {
            InstituteDoesNotExistException.class,
            TalentDoesNotExistException.class,
            MitraDoesNotExistException.class,
            RoleDoesNotExistException.class,
            VacancyNotExistException.class,
            AccountDoesNotExistException.class,
            UserDoesNotExistException.class,
            ApplicantDoesNotExistException.class,
            ClientDoesNotExistException.class,
            InterviewScheduleDoesNotExistException.class,
            RecommendationDoesNotExistException.class,
            PrivilegeDoesNotExistException.class
    })
    public ResponseEntity<Object> resourceNotAvailableHandler(Exception e) {
        return ResponseHandler.generateResponse(new Response(
                e.getMessage(), HttpStatus.NOT_FOUND, FAILED, null));
    }

    @ExceptionHandler(value = {
            TalentAlreadyExistException.class,
            ApplicantAlreadyExistsException.class,
            AccountAlreadyExistException.class
    })
    public ResponseEntity<Object> resourceAlreadyExistHandler(Exception e) {
        return ResponseHandler.generateResponse(new Response(
                e.getMessage(), HttpStatus.CONFLICT, FAILED, null));
    }

    @ExceptionHandler(value = {
            InvalidTalentNikException.class,
            AccountInvalidPasswordException.class,
            ApplicantNotRecommendedException.class,
            InterviewScheduleConflictException.class,
            InterviewScheduleUpdateBadRequestException.class,
            IncompleteVacancyRequestException.class,
            IncompleteTalentCvException.class,
            InstituteTypeDoesNotExistException.class,
            VacancyStatusDoesNotExistException.class,
    })
    public ResponseEntity<Object> badRequestHandler(Exception e) {
        return ResponseHandler.generateResponse(new Response(
                e.getMessage(), HttpStatus.BAD_REQUEST, FAILED, null));
    }
}
